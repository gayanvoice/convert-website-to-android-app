/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.gayanvoice.app.component.activity

import android.os.Bundle
import com.microsoft.fluentui.persona.IPersona
import com.microsoft.fluentui.persona.PersonaListView
import com.microsoft.fluentui.snackbar.Snackbar
import com.gayanvoice.app.R
import com.gayanvoice.app.util.createPersonaList
import kotlinx.android.synthetic.main.activity_demo_detail.*
import kotlinx.android.synthetic.main.activity_persona_list_view.*

class PersonaListViewActivity : com.gayanvoice.app.AppActivity() {
    override val contentLayoutId: Int
        get() = R.layout.activity_persona_list_view

    override val contentNeedsScrollableContainer: Boolean
        get() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        app_bar.scrollTargetViewId = R.id.persona_list_view_example

        persona_list_view_example.personas = createPersonaList(this)
        persona_list_view_example.onItemClickedListener = object : PersonaListView.OnItemClickedListener {
            override fun onItemClicked(persona: IPersona) {
                Snackbar.make(
                    root_view,
                    "You clicked on the cell for ${persona.name}, ${persona.subtitle}",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }
}