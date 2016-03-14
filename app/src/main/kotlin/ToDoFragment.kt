/**
 * Created by sergezotov on 12.03.16.
 */
package com.srgzotov.hellokotlin;

import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import org.jetbrains.anko.*
import org.jetbrains.anko.design.coordinatorLayout
import org.jetbrains.anko.design.floatingActionButton
import org.jetbrains.anko.recyclerview.v7.recyclerView
import java.util.logging.Logger

class ToDoFragment : Fragment() {

    companion object {
        val logger = Logger.getLogger(ToDoFragment::class.java.name)
    }
    var items: MutableList<ToDo> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return FragmentUi<ToDoFragment>().createView(AnkoContext.create(ctx, this))
    }

    fun addToDo(name: String, desc: String) {
        logger.info("New TODO: $name with description $desc")
        items.add(ToDo(name, desc))
    }
}

class FragmentUi<T> : AnkoComponent<T> {

    override fun createView(ui: AnkoContext<T>) = with(ui) {
        coordinatorLayout {
            var btnOk: View? = null
            var btnCancel: View? = null
            var editText1: EditText? = null
            var editText2: EditText? = null
            val overlay = frameLayout {
                id = Ids.overlay_frame
                visibility = View.GONE
                backgroundColor = 0xFFFFFFFF.toInt()
                relativeLayout {
                    editText1 = editText {
                        id = Ids.edit_todo_name
                        lparams (width = matchParent) {
                            alignParentLeft()
                        }
                    }
                    editText2 = editText {
                        id = Ids.edit_todo_desc
                        lparams (width = matchParent) {
                            below(Ids.edit_todo_name)
                        }
                    }
                    btnOk = button {
                        id = Ids.edit_todo_btn_ok
                        text = "Ok"
                        lparams {
                            below(Ids.edit_todo_desc)
                        }
                    }
                    btnCancel = button {
                        text = "Cancel"
                        lparams {
                            below(Ids.edit_todo_desc)
                            rightOf(Ids.edit_todo_btn_ok)
                        }
                    }
                }.lparams {
                    width = matchParent
                }
            }.lparams {
                width = dip(250)
                gravity = Gravity.CENTER
            }

            btnOk?.onClick {
                (ui.owner as ToDoFragment).addToDo(editText1?.text.toString(), editText2?.text.toString())
                editText1?.editableText?.clear()
                editText2?.editableText?.clear()
                toggleVisibility(overlay)
            }

            btnCancel?.onClick {
                toggleVisibility(overlay)
            }

            frameLayout {
                textView {}
                val recycler = recyclerView {}
                recycler.adapter = ToDoAdapter((ui.owner as ToDoFragment).items)
                recycler.layoutManager = LinearLayoutManager(context)

                floatingActionButton {
                    lparams {
                        margin = dip(30)
                    gravity = Gravity.BOTTOM or Gravity.RIGHT
                    }
                    imageResource = R.drawable.ic_add_white_24dp
                    onClick {
                        toggleVisibility(overlay)
                    }
                }
            }
            overlay.bringToFront()
        }
    }

    fun toggleVisibility(view: View) {
        when (view.isShown) {
            true -> view.visibility = View.GONE
            false -> view.visibility = View.VISIBLE
        }
    }

    companion object Ids {
        val overlay_frame = 1001
        val edit_todo_name = 1002
        val edit_todo_desc = 1003
        val edit_todo_btn_ok = 1004
    }
}

class EmptyToDoViewHolder(val view: View): RecyclerView.ViewHolder(view) {
    companion object Factory {
        fun create(context: Context): EmptyToDoViewHolder {
            return EmptyToDoViewHolder(
                    context.verticalLayout {
                        lparams {
                            width = matchParent
                        }
                        textView {
                            lparams {
                                topMargin = dip(20)
                                gravity = Gravity.CENTER_HORIZONTAL
                            }
                            text = "Nothing to show so far"
                        }
                    }
            )
        }
    }
}

class ToDoViewHolder(val view: View, val nameView: TextView, val descriptionView: TextView,
                     val checkBoxView: CheckBox) : RecyclerView.ViewHolder(view) {

    var item: ToDo? = null

    fun bind(item: ToDo): Unit {
        this.item = item
        nameView.text = item.name
        descriptionView.text = item.description
        checkBoxView.isChecked = item.completed
    }

    companion object Factory {
        fun create(context: Context): ToDoViewHolder {
            var nameView: TextView? = null
            var descView: TextView? = null
            var checkBoxView: CheckBox? = null
            val holder = ToDoViewHolder(
                    context.relativeLayout {

                        padding = dip(10)

                        nameView = textView {
                            lparams(width = dip(15), height = wrapContent) {
                                id = Ids.textView1
                                addRule(RelativeLayout.ALIGN_PARENT_LEFT)
                            }
                        }

                        descView = textView {
                            lparams {
                                id = Ids.textView2
                                addRule(RelativeLayout.RIGHT_OF, Ids.textView1)
                            }
                        }

                        checkBoxView = checkBox {
                            lparams {
                                addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                            }
                        }
                    }
                    , nameView!!, descView!!, checkBoxView!!)
            checkBoxView?.setOnCheckedChangeListener { compoundButton, b -> holder.toggleCompletion() }
            return holder
        }

        private object Ids {
            val checkBox = 1
            val textView1 = 2
            val textView2 = 3
        }
    }

    fun toggleCompletion() {
        item?.completed = item?.completed?: false
    }

}

class ToDoAdapter(val items: List<ToDo>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder?, p1: Int) {
        if (p0 is ToDoViewHolder)
            p0?.bind(items[p1])
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup?, viewType: Int): RecyclerView.ViewHolder? {
        when (viewType) {
            ViewTypes.EMPTY -> return EmptyToDoViewHolder.create(viewGroup?.context!!)
            else -> return ToDoViewHolder.create(viewGroup?.context!!)
        }
    }

    override fun getItemCount(): Int {
        if (items.size > 0)
            return items.size
        else
            return 1;
    }

    override fun getItemViewType(position: Int): Int {
        if (items.size == 0) {
            return ViewTypes.EMPTY
        }
        else {
            return ViewTypes.NORMAL
        }
    }

    companion object ViewTypes {
        val NORMAL = 1000
        val EMPTY = 0
    }
}