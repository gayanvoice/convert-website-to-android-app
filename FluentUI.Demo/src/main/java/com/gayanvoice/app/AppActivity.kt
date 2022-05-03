package com.gayanvoice.app

import android.content.Intent
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import com.microsoft.fluentui.util.DuoSupportUtils
import kotlinx.android.synthetic.main.activity_demo_detail.*
import java.util.*

abstract class AppActivity : AppCompatActivity() {
    companion object {
        const val DEMO_ID = "demo_id"
    }

    protected abstract val contentLayoutId: Int
        @LayoutRes get
    protected open val contentNeedsScrollableContainer: Boolean
        get() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(com.gayanvoice.app.R.layout.activity_demo_detail)

        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Set demo title
        val demoID = intent.getSerializableExtra(com.gayanvoice.app.AppActivity.Companion.DEMO_ID) as UUID
        var demo: com.gayanvoice.app.model.Demo?
        if(DuoSupportUtils.isDualScreenMode(this)){
            demo = com.gayanvoice.app.model.DUO_DEMOS.find{ it.id == demoID }
        }
        else{
            demo = com.gayanvoice.app.model.DEMOS.find{ it.id == demoID }
        }
        title = demo?.title

        val container = if (contentNeedsScrollableContainer) demo_detail_scrollable_container else demo_detail_container
        layoutInflater.inflate(contentLayoutId, container, true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                navigateUpTo(Intent(this, com.gayanvoice.app.MainActivity::class.java))
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}
