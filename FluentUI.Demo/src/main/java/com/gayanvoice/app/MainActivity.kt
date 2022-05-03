package com.gayanvoice.app

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.gayanvoice.app.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.microsoft.fluentui.bottomsheet.BottomSheetAdapter
import com.microsoft.fluentui.bottomsheet.BottomSheetItem
import com.microsoft.fluentui.listitem.ListItemView
import com.microsoft.fluentui.persistentbottomsheet.PersistentBottomSheet
import com.microsoft.fluentui.persistentbottomsheet.SheetItem
import com.microsoft.fluentui.search.Searchbar
import com.microsoft.fluentui.util.DuoSupportUtils
import kotlinx.android.synthetic.main.activity_demo_detail.*
import kotlinx.android.synthetic.main.activity_demo_list.*
import kotlinx.android.synthetic.main.activity_demo_list.app_bar
import kotlinx.android.synthetic.main.activity_persistent_bottom_sheet.*
import kotlinx.android.synthetic.main.activity_progress.*
import kotlinx.android.synthetic.main.demo_persistent_sheet_content.*

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener, BottomSheetItem.OnClickListener  {
    private lateinit var searchbar: Searchbar
    var dualScreenMode = false


    // new
    private lateinit var persistentBottomSheetDemo: PersistentBottomSheet
    private lateinit var defaultPersistentBottomSheet: PersistentBottomSheet
    private lateinit var defaultPersistentBottomSheetContent: View
    private lateinit var currentSheet: PersistentBottomSheet
    private lateinit var scrollView: ScrollView
    private var isBack: Boolean = false
    private lateinit var view: TextView
    private lateinit var mHorizontalSheet: MutableList<SheetItem>
//    private lateinit var mHorizontalSheet2: MutableList<SheetItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        // Launch Screen: Setting the theme here removes the launch screen, which was added to this activity
        // by setting the theme to App.Launcher in the manifest.
        setTheme(R.style.AppTheme)

        super.onCreate(savedInstanceState)
        dualScreenMode = DuoSupportUtils.isDualScreenMode(this)

        // new
        setContentView(R.layout.app_activity_persistent_bottom_sheet)

//        val progress_bar_linear_indeterminate = findViewById(R.id.progress_bar_linear_indeterminate_1)
//        var progressStatus = 0
//        val handler = Handler()
//
//        // Updates linear determinate progress
//        Thread {
//            while(progressStatus<100){
//                progressStatus++
//                handler.post {
//                    progress_bar_linear_indeterminate.progress = progressStatus
//                }
//                Thread.sleep(20)
//                if(progressStatus == 100)
//                    progressStatus = 0
//            }
//        }.start()

        persistentBottomSheetDemo = findViewById(R.id.demo_persistent_sheet)
        defaultPersistentBottomSheet = findViewById(R.id.default_persistent_sheet)
        scrollView = findViewById(R.id.scroll_container)

        defaultPersistentBottomSheetContent = LayoutInflater.from(this).inflate(R.layout.app_demo_persistent_sheet_content, null)

        PersistentBottomSheet.DefaultContentBuilder(this)
                .setCustomSheetContent(defaultPersistentBottomSheetContent)
                .buildWith(persistentBottomSheetDemo)

        persistentBottomSheetDemo.setDrawerHandleContentDescription(getString(R.string.drawer_content_desc_collapse_state),
                getString(R.string.drawer_content_desc_expand_state))

        mHorizontalSheet = arrayListOf(
                SheetItem(
                        R.id.bottom_sheet_item_flag,
                        getString(R.string.bottom_sheet_item_flag_title),
                        R.drawable.ic_fluent_flag_24_regular,
                        ContextCompat.getColor(this, R.color.bottomsheet_horizontal_icon_tint),
                        disabled = false),
                SheetItem(R.id.bottom_sheet_item_alarm,
                        getString(R.string.bottom_sheet_item_custom_image),
                        dummyBitmap(),
                        disabled = true),
                SheetItem(
                        R.id.persistent_sheet_item_add_view,
                        getString(R.string.persistent_sheet_item_add_remove_view),
                        R.drawable.ic_add_circle_28_fill,
                        ContextCompat.getColor(this, R.color.bottomsheet_horizontal_icon_tint),
                        disabled = false),
                SheetItem(
                        R.id.persistent_sheet_item_change_height_button,
                        getString(R.string.persistent_sheet_item_change_collapsed_height),
                        R.drawable.ic_vertical_align_center_28_fill,
                        ContextCompat.getColor(this, R.color.bottomsheet_horizontal_icon_tint),
                        disabled = true
                ),
                SheetItem(
                        R.id.bottom_sheet_item_reply,
                        getString(R.string.bottom_sheet_item_reply_title),
                        R.drawable.ic_fluent_reply_24_regular,
                        ContextCompat.getColor(this, R.color.bottomsheet_horizontal_icon_tint),
                        disabled = false
                ),
                SheetItem(
                        R.id.bottom_sheet_item_forward,
                        getString(R.string.bottom_sheet_item_forward_title),
                        R.drawable.ic_fluent_forward_24_regular,
                        ContextCompat.getColor(this, R.color.bottomsheet_horizontal_icon_tint),
                        disabled = true
                ),
                SheetItem(
                        R.id.bottom_sheet_item_delete,
                        getString(R.string.bottom_sheet_item_delete_title),
                        R.drawable.ic_delete_24_regular,
                        ContextCompat.getColor(this, R.color.bottomsheet_horizontal_icon_tint),
                        disabled = false
                ),
                SheetItem(
                        R.id.bottom_sheet_item_delete,
                        getString(R.string.bottom_sheet_item_delete_title),
                        R.drawable.ic_delete_24_regular,
                        ContextCompat.getColor(this, R.color.bottomsheet_horizontal_icon_tint),
                        disabled = true
                ),
                SheetItem(
                        R.id.bottom_sheet_item_delete,
                        getString(R.string.bottom_sheet_item_delete_title),
                        R.drawable.ic_delete_24_regular,
                        ContextCompat.getColor(this, R.color.bottomsheet_horizontal_icon_tint),
                        disabled = false
                )
        )


//        mHorizontalSheet2 = arrayListOf(
//                SheetItem(
//                        R.id.bottom_sheet_item_flag,
//                        getString(R.string.bottom_sheet_item_flag_title),
//                        R.drawable.ic_fluent_flag_24_regular,
//                        ContextCompat.getColor(this, R.color.bottomsheet_horizontal_icon_tint),
//                        disabled = true),
//                SheetItem(R.id.bottom_sheet_item_alarm,
//                        getString(R.string.bottom_sheet_item_custom_image),
//                        dummyBitmap(),
//                        disabled = false),
//                SheetItem(
//                        R.id.persistent_sheet_item_add_view,
//                        getString(R.string.persistent_sheet_item_add_remove_view),
//                        R.drawable.ic_add_circle_28_fill,
//                        ContextCompat.getColor(this, R.color.bottomsheet_horizontal_icon_tint),
//                        disabled = true),
//                SheetItem(
//                        R.id.persistent_sheet_item_change_height_button,
//                        getString(R.string.persistent_sheet_item_change_collapsed_height),
//                        R.drawable.ic_vertical_align_center_28_fill,
//                        ContextCompat.getColor(this, R.color.bottomsheet_horizontal_icon_tint),
//                        disabled = false),
//                SheetItem(
//                        R.id.bottom_sheet_item_reply,
//                        getString(R.string.bottom_sheet_item_reply_title),
//                        R.drawable.ic_fluent_reply_24_regular,
//                        ContextCompat.getColor(this, R.color.bottomsheet_horizontal_icon_tint),
//                        disabled = true)
//        )


        sheet_horizontal_item_list_1.createHorizontalItemLayout(mHorizontalSheet)
//        sheet_horizontal_item_list_1.setTextAppearance(R.style.TextAppearance_FluentUI_HorizontalListItemTitle)

//        sheet_horizontal_item_list_2.createHorizontalItemLayout(mHorizontalSheet2)
//        sheet_horizontal_item_list_2.setTextAppearance(R.style.TextAppearance_FluentUI_HorizontalListItemTitle)

//        val marginBetweenView = resources.getDimension(R.dimen.fluentui_persistent_horizontal_item_right_margin).toInt()
//        val horizontalListAdapter = SheetHorizontalItemAdapter(this,
//                arrayListOf(
//                        SheetItem(
//                                R.id.persistent_sheet_item_create_new_folder,
//                                getString(R.string.persistent_sheet_item_create_new_folder_title),
//                                R.drawable.ic_create_new_folder_24_filled,
//                                ContextCompat.getColor(this, R.color.bottomsheet_horizontal_icon_tint),
//                                disabled = true
//                        ),
//                        SheetItem(
//                                R.id.persistent_sheet_item_edit,
//                                getString(R.string.persistent_sheet_item_edit_title),
//                                R.drawable.ic_edit_24_filled,
//                                ContextCompat.getColor(this, R.color.bottomsheet_horizontal_icon_tint),
//                                disabled = false
//                        ),
//                        SheetItem(
//                                R.id.persistent_sheet_item_save,
//                                getString(R.string.persistent_sheet_item_save_title),
//                                R.drawable.ic_save_24_filled,
//                                ContextCompat.getColor(this, R.color.bottomsheet_horizontal_icon_tint),
//                                disabled = true
//                        ),
//                        SheetItem(
//                                R.id.persistent_sheet_item_zoom_in,
//                                getString(R.string.persistent_sheet_item_zoom_in_title),
//                                R.drawable.ic_zoom_in_24_filled,
//                                ContextCompat.getColor(this, R.color.bottomsheet_horizontal_icon_tint),
//                        ),
//                        SheetItem(
//                                R.id.persistent_sheet_item_zoom_out,
//                                getString(R.string.persistent_sheet_item_zoom_out_title),
//                                R.drawable.ic_zoom_out_24_filled,
//                                ContextCompat.getColor(this, R.color.bottomsheet_horizontal_icon_tint),
//                                disabled = true
//                        )
//                ), R.style.Drawer_FluentUI, marginBetweenView)
//        horizontalListAdapter.mOnSheetItemClickListener = this
//        sheet_horizontal_item_list_3.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
//        sheet_horizontal_item_list_3.adapter = horizontalListAdapter


        val verticalListAdapter = BottomSheetAdapter(this,
                arrayListOf(
                        BottomSheetItem(
                                R.id.bottom_sheet_item_camera,
                                R.drawable.ic_camera_24_regular,
                                getString(R.string.bottom_sheet_item_camera_title),
                                getString(R.string.bottom_sheet_item_camera_subtitle),
                                disabled = false
                        ),
                        BottomSheetItem(
                                R.id.bottom_sheet_item_gallery,
                                R.drawable.ic_image_library_24_regular,
                                getString(R.string.bottom_sheet_item_gallery_title),
                                getString(R.string.bottom_sheet_item_gallery_subtitle),
                                disabled = false
                        ),
                        BottomSheetItem(
                                R.id.bottom_sheet_item_videos,
                                R.drawable.ic_video_24_regular,
                                getString(R.string.bottom_sheet_item_videos_title),
                                getString(R.string.bottom_sheet_item_videos_subtitle),
                                disabled = false
                        ),
                        BottomSheetItem(
                                R.id.bottom_sheet_item_manage,
                                R.drawable.ic_settings_24_regular,
                                getString(R.string.bottom_sheet_item_manage_title),
                                getString(R.string.bottom_sheet_item_manage_subtitle),
                                disabled = false
                        )
                ), 0)
        verticalListAdapter.onBottomSheetItemClickListener = this
        sheet_vertical_item_list_1.adapter = verticalListAdapter


//        webview.webViewClient = MyWebViewClient(this)
//        webview.loadUrl("https://www.javatpoint.com/")



//        val myWebView: WebView = view.findViewById(R.id.webview)
//        myWebView.loadUrl("https://google.com")




//        setContentView(R.layout.activity_demo_list)

        setupAppBar()

//        demo_list.adapter = DemoListAdapter()
//        demo_list.addItemDecoration(ListItemDivider(this, DividerItemDecoration.VERTICAL))

        setUpWebView("https://www.bbc.com/sinhala")

        Initializer.init(application)
    }

    private fun setUpWebView(website: String?){
        val myWebView: WebView = findViewById(R.id.webview)
        myWebView.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        })
        myWebView.loadUrl(website)

//        val myWebView: WebView = findViewById(R.id.webview)
//        myWebView.webViewClient = MyWebViewClient()

    }


    private class MyWebViewClient : WebViewClient() {

//        fun AppWebViewClients(progressBar: ProgressBar) {
//            progressBar = progressBar
//            progressBar.setVisibility(View.VISIBLE)
//        }
        override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
            if (Uri.parse(url).host == "https://timesofindia.indiatimes.com/") {
                return false
            }
//            Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
//                startActivity(this)
//            }
            return true
        }
    }


    private fun showHideBottomSheet(show: Boolean) {
        if (show && currentSheet.getBottomSheetBehaviour().state == BottomSheetBehavior.STATE_HIDDEN) {
            currentSheet.show()
        } else if (!show) {
            currentSheet.hide()
        }
    }

    private fun dummyBitmap(): Bitmap {
        val option = BitmapFactory.Options()
        option.outHeight = resources.getDimensionPixelSize(R.dimen.image_size)
        option.outWidth = resources.getDimensionPixelSize(R.dimen.image_size)
        return Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, R.drawable.avatar_allan_munger), option.outWidth, option.outHeight, false)
    }





    override fun onBottomSheetItemClick(item: BottomSheetItem) {
        when (item.id) {
            R.id.bottom_sheet_item_camera -> {
                setUpWebView("https://www.bbc.com/sinhala/topics/cg7267dz901t")
            }
            R.id.bottom_sheet_item_gallery -> setUpWebView("https://www.bbc.com/sinhala/topics/c83plvepnq1t")
            R.id.bottom_sheet_item_videos -> setUpWebView("https://www.bbc.com/sinhala/51727586")
            R.id.bottom_sheet_item_manage -> {
//                val demo = view.tag as Demo
//                val intent = Intent(view.context, demo.demoClass.java)
//                intent.putExtra(AppActivity.DEMO_ID, demo.id)
//                view.context.startActivity(intent)

            }
        }
    }












    private fun setupAppBar() {
        app_bar.toolbar.subtitle = BuildConfig.VERSION_NAME
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return false
    }

    override fun onQueryTextChange(query: String): Boolean {
        val userInput = query.toLowerCase()
        val demoList: ArrayList<Demo> = if(dualScreenMode) DUO_DEMOS else DEMOS
        val filteredDemoList = demoList.filter { it.title.toLowerCase().contains(userInput) }

        searchbar.showSearchProgress = true

        Handler().postDelayed({
            (demo_list.adapter as DemoListAdapter).demos = filteredDemoList as ArrayList<Demo>
            searchbar.showSearchProgress = false
        }, 500)
        return true
    }

    private inner class DemoListAdapter : RecyclerView.Adapter<DemoListAdapter.ViewHolder>() {
        var demos = if(dualScreenMode) DUO_DEMOS else DEMOS
            set(value) {
                field = value
                notifyDataSetChanged()
            }
        private val onClickListener = View.OnClickListener { view ->
            val demo = view.tag as Demo
            val intent = Intent(view.context, demo.demoClass.java)
            intent.putExtra(AppActivity.DEMO_ID, demo.id)
            view.context.startActivity(intent)
        }

        override fun getItemCount(): Int = demos.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val listItemView = ListItemView(parent.context)
            listItemView.layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            return ViewHolder(listItemView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val demo = demos[position]
            holder.listItem.title = demo.title
            with(holder.itemView) {
                tag = demo
                setOnClickListener(onClickListener)
            }
        }

        private inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val listItem: ListItemView = view as ListItemView
        }
    }
}