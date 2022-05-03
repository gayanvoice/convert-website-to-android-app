/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.gayanvoice.app.component.activity

import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.widget.LinearLayout
import com.microsoft.fluentui.peoplepicker.PeoplePickerAccessibilityTextProvider
import com.microsoft.fluentui.peoplepicker.PeoplePickerPersonaChipClickStyle
import com.microsoft.fluentui.peoplepicker.PeoplePickerView
import com.microsoft.fluentui.persona.IPersona
import com.microsoft.fluentui.snackbar.Snackbar
import com.gayanvoice.app.R
import com.gayanvoice.app.util.createCustomPersona
import com.gayanvoice.app.util.createPersonaList
import kotlinx.android.synthetic.main.activity_demo_detail.*
import kotlinx.android.synthetic.main.activity_people_picker_view.*
import java.util.*
import kotlin.collections.ArrayList

class PeoplePickerViewActivity : com.gayanvoice.app.AppActivity() {
    override val contentLayoutId: Int
        get() = R.layout.activity_people_picker_view

    private lateinit var samplePersonas: ArrayList<IPersona>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        samplePersonas = createPersonaList(this)
        val accessibilityTextProvider = getAccessibilityTextProvider()

        // Use attributes to set personaChipClickStyle and label

        people_picker_select.availablePersonas = samplePersonas
        val selectPickedPersonas = arrayListOf(
            samplePersonas[0],
            samplePersonas[1],
            samplePersonas[4],
            samplePersonas[5]
        )
        val selectSearchDirectoryPersonas = arrayListOf(
            samplePersonas[14],
            samplePersonas[7],
            samplePersonas[8],
            samplePersonas[9]
        )
        people_picker_select.pickedPersonas = selectPickedPersonas
        people_picker_select.showSearchDirectoryButton = true
        people_picker_select.searchDirectorySuggestionsListener = createPersonaSuggestionsListener(selectSearchDirectoryPersonas)
        people_picker_select.allowPersonaChipDragAndDrop = true
        people_picker_select.onCreatePersona = { name, email ->
            createCustomPersona(this, name, email)
        }
        people_picker_select.accessibilityTextProvider = accessibilityTextProvider

        people_picker_select_deselect.availablePersonas = samplePersonas
        val selectDeselectPickedPersonas = arrayListOf(samplePersonas[2])
        people_picker_select_deselect.pickedPersonas = selectDeselectPickedPersonas
        people_picker_select_deselect.allowPersonaChipDragAndDrop = true
        people_picker_select_deselect.accessibilityTextProvider = accessibilityTextProvider
        people_picker_select_deselect.personaChipClickListener = object : PeoplePickerView.PersonaChipClickListener {
            override fun onClick(persona: IPersona) {
                showSnackbar(getString(R.string.people_picker_persona_chip_click, accessibilityTextProvider.getPersonaDescription(persona)))
            }
        }

        // Use code to set personaChipClickStyle and label

        setupPeoplePickerView(
            "",
            samplePersonas,
            PeoplePickerPersonaChipClickStyle.NONE,
            valueHint = getString(R.string.people_picker_hint),
            showHint = true
        )
        setupPeoplePickerView(
            getString(R.string.people_picker_delete_example),
            samplePersonas,
            PeoplePickerPersonaChipClickStyle.DELETE
        )
        setupPeoplePickerView(
            getString(R.string.people_picker_picked_personas_listener),
            samplePersonas,
            pickedPersonasChangeListener = createPickedPersonasChangeListener()
        )
        setupPeoplePickerView(
            getString(R.string.people_picker_suggestions_listener),
            personaSuggestionsListener = createPersonaSuggestionsListener(samplePersonas)
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
            people_picker_select.requestFocus()
    }

    private fun getAccessibilityTextProvider() = object : PeoplePickerAccessibilityTextProvider(resources) {
        override fun getPersonaQuantityText(personas: ArrayList<IPersona>): String {
            return resources.getQuantityString(
                R.plurals.people_picker_accessibility_text_view_example,
                personas.size,
                personas.size
            )
        }
    }

    private fun setupPeoplePickerView(
        labelText: String,
        availablePersonas: ArrayList<IPersona> = ArrayList(),
        personaChipClickStyle: PeoplePickerPersonaChipClickStyle = PeoplePickerPersonaChipClickStyle.SELECT,
        personaSuggestionsListener: PeoplePickerView.PersonaSuggestionsListener? = null,
        pickedPersonasChangeListener: PeoplePickerView.PickedPersonasChangeListener? = null,
        valueHint: String = "",
        showHint: Boolean = false
    ) {
        val peoplePickerView = PeoplePickerView(this)
        peoplePickerView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        with(peoplePickerView) {
            label = labelText
            this.availablePersonas = availablePersonas
            this.personaChipClickStyle = personaChipClickStyle
            this.personaSuggestionsListener = personaSuggestionsListener
            this.pickedPersonasChangeListener = pickedPersonasChangeListener
            allowPersonaChipDragAndDrop = true
            this.valueHint = valueHint
            this.showHint = showHint
        }
        people_picker_layout.addView(peoplePickerView)
    }

    private fun createPickedPersonasChangeListener(): PeoplePickerView.PickedPersonasChangeListener {
        return object : PeoplePickerView.PickedPersonasChangeListener {
            override fun onPersonaAdded(persona: IPersona) {
                showSnackbar("${getString(R.string.people_picker_dialog_title_added)} ${if (persona.name.isNotEmpty()) persona.name else persona.email}")
            }

            override fun onPersonaRemoved(persona: IPersona) {
                showSnackbar("${getString(R.string.people_picker_dialog_title_removed)} ${if (persona.name.isNotEmpty()) persona.name else persona.email}")
            }
        }
    }

    private fun createPersonaSuggestionsListener(personas: ArrayList<IPersona>): PeoplePickerView.PersonaSuggestionsListener {
        return object : PeoplePickerView.PersonaSuggestionsListener {
            override fun onGetSuggestedPersonas(
                searchConstraint: CharSequence?,
                availablePersonas: ArrayList<IPersona>?,
                pickedPersonas: ArrayList<IPersona>,
                completion: (suggestedPersonas: ArrayList<IPersona>) -> Unit
            ) {
                // Simulating async filtering with Timer
                Timer().schedule(
                    object : TimerTask() {
                        override fun run() {
                            completion(filterPersonas(searchConstraint, personas, pickedPersonas))
                        }
                    },
                    500
                )
            }
        }
    }

    private fun showSnackbar(text: String) {
        Snackbar.make(root_view, text, Snackbar.LENGTH_SHORT).show()
    }

    // Basic custom filtering example
    private fun filterPersonas(
        searchConstraint: CharSequence?,
        availablePersonas: ArrayList<IPersona>,
        pickedPersonas: ArrayList<IPersona>
    ): ArrayList<IPersona> {
        if (searchConstraint == null)
            return availablePersonas
        val constraint = searchConstraint.toString().toLowerCase()
        val filteredResults = availablePersonas.filter {
            it.name.toLowerCase().contains(constraint) && !pickedPersonas.contains(it)
        }
        return ArrayList(filteredResults)
    }
}