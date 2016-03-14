/**
 * Created by sergezotov on 12.03.16.
 */
package com.srgzotov.hellokotlin;

import android.app.Activity
import android.os.Bundle
import org.jetbrains.anko.frameLayout
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.verticalLayout
import org.jetbrains.anko.wrapContent

class AppMainActivity: Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        verticalLayout {
            lparams {
                width = matchParent
                height = wrapContent
            }
            frameLayout {
                id = 100001
            }.lparams {
                width = matchParent
                height = wrapContent
            }
        }
        fragmentManager.beginTransaction().replace(100001, ToDoFragment()).commit()
    }
}