/*
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License.
 */

package com.gayanvoice.app.component.list

import android.view.View

interface IButtonItem : IBaseListItem {
    var buttonText: String
    var id: Int
    var onClickListener: View.OnClickListener
}

data class ButtonItem(
    override var title: String = "",
    override var buttonText: String,
    override var id: Int,
    override var onClickListener: View.OnClickListener
) : IButtonItem