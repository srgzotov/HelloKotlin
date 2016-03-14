/**
 * Created by sergezotov on 06.03.16.
 */
package com.srgzotov.hellokotlin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewManager
import android.widget.EditText
import android.widget.TextView
import kotlinx.android.synthetic.main.layout_main.*
import org.jetbrains.anko.*
import org.jetbrains.anko.cardview.v7.cardView
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.recyclerview.v7.recyclerView
import java.util.logging.Logger

class HelloActivity: Activity() {

    companion object {
        val logger = Logger.getLogger(HelloActivity::class.java.name)
    }

    var holder: HelloViewHolder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        holder = HelloViewHolder.create(this)
    }

    fun login(username: String, pwd: String): Unit {
        logger.info("Login username = $username, pwd = $pwd")
        startActivity<AppMainActivity>()
    }
}

class HelloViewHolder(val activity: HelloActivity) {

    val username: String
    get() = usernameView?.text.toString()

    val pwd: String
    get() = pwdView?.text.toString()

    var usernameView: TextView? = null
    var pwdView: TextView? = null

    companion object Factory {
        fun create(activity: HelloActivity): HelloViewHolder {
            val holder = HelloViewHolder(activity)
            holder.createUI()
            return holder
        }
    }

    fun createUI(): Unit {
        activity.verticalLayout {
            padding = dip(30)
            usernameView = editText {
                hint = "Name"
                textSize = 24f
            }

            pwdView = editText {
                hint = "Password"
                textSize = 24f
            }
            button("Login") {
                textSize = 26f
            }.onClick { activity.login(usernameView?.text.toString(), pwdView?.text.toString()) }

        }.style { view ->
            when (view) {
                is EditText -> view.textSize = 20f
            }
        }
    }
}