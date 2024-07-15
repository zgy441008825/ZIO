package com.zougy.zio

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.media.MediaMetadataRetriever
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.bm.library.PhotoView
import com.chad.library.adapter4.BaseQuickAdapter
import com.chad.library.adapter4.QuickAdapterHelper
import com.chad.library.adapter4.loadState.LoadState
import com.chad.library.adapter4.loadState.trailing.TrailingLoadStateAdapter
import com.chad.library.adapter4.viewholder.QuickViewHolder
import com.zougy.log.LogUtils
import com.zougy.tools.ThreadUtils
import com.zougy.ui.widget.setting.SettingTabControl
import com.zougy.ziolib.files.FileTypeEnum
import com.zougy.ziolib.files.ZFileTools
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.schedulers.Schedulers
import org.xutils.x
import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit


class MyViewPagerAdapter : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val photoView = PhotoView(x.app())
        photoView.enable()
        photoView.maxScale = 3f
        container.addView(photoView)
        /*Glide.with(photoView)
            .load(R.drawable.img_test)
            .into(photoView)*/
        return photoView
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as PhotoView)
    }

    override fun getCount(): Int {
        return 10
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view.equals(`object`)
    }

}

class MainActivity : BaseActivity() {

    private val fileBeanList = mutableListOf<FileBean>()

    companion object {
        const val TAG = "MainActivity"

        const val PAGE_SIZE = 50
    }

    lateinit var filePath: File

    private val executors = Executors.newSingleThreadExecutor()

    private val myAdapter = MyAdapter()

    private lateinit var adapterHelper: QuickAdapterHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ThreadUtils.runBackgroundThread {
            val fileList = ZFileTools.searchFile("/storage/emulated/0/Pictures", FileTypeEnum.FILE_TYPE_IMG, null, true, true)
            LogUtils.i(TAG, "onCreate fileList:${fileList.size}")
            fileList.forEach {
                LogUtils.i(TAG, "onCreate file:$it")
            }
        }
//        val acMainTabControl = findViewById<SettingTabControl>(R.id.acMainTabControl)

        /*val list = mutableListOf<FileBean>()
        for (i in 0..10) {
            list.add(FileBean(File("123.txt"), 0, 0, 0))
        }
        val recyclerView = findViewById<RecyclerView>(R.id.acMainRecyclerView)
        myAdapter.submitList(list)
        recyclerView.adapter = myAdapter
        val de = RecyclerViewItemDecoration(this)
        de.apply {
            setDrawable(R.color.colorAccent)
            setPadding(10, 10, 10, 10)
            spaceSize = 1
        }
        recyclerView.addItemDecoration(de)
//        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = GridLayoutManager(this, 4)*/
    }

    private fun initFiles() {
        val p = cacheDir
        filePath = File(p, "第1回.mp4")
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 10)
        } else {
            /*要实现上拉加载更多，在初始化完第一次的数据时就需要设置adapterHelper.trailingLoadState的状态为NotLoading，如果还有数据，传false，没有数据传true
            第一次设置数据使用submitList方法，需要注意的是，adapter中时直接使用的添加的列表对象，所以后续加载更多时，不要用原list.addAll之后再调用adapter.addAll，只需要
            调用adapter.addAll即可。
             */
            loadFileBean(fileBeanList.size, PAGE_SIZE) {
                fileBeanList.addAll(it)
                myAdapter.submitList(fileBeanList)
                adapterHelper.trailingLoadState = LoadState.NotLoading(false)
            }
        }

        val loadAdapter = LoadStateAdapter().apply {
            preloadSize = 30
            isAutoLoadMore = true
            setOnLoadMoreListener(object : TrailingLoadStateAdapter.OnTrailingListener {
                override fun onFailRetry() {
                    LogUtils.i(TAG, "onFailRetry")
                }

                override fun onLoad() {
                    LogUtils.i(TAG, "onLoad")
                    /*
                     * 注意:adapter中时直接使用的添加的列表对象，所以后续加载更多时，不要用原list.addAll之后再调用adapter.addAll，
                     * 只需要调用adapter.addAll即可。
                     */
                    loadFileBean(fileBeanList.size + 1, PAGE_SIZE) {
                        LogUtils.i(TAG, "onLoad before fileBeanList:${fileBeanList.size} it:${it.size}")
//                        fileBeanList.addAll(it)
                        myAdapter.addAll(it)
                        LogUtils.i(TAG, "onLoad end fileBeanList:${fileBeanList.size}")
                        adapterHelper.trailingLoadState = LoadState.NotLoading(fileBeanList.size > 9999)
                    }
                }

                override fun isAllowLoading(): Boolean {
                    return super.isAllowLoading()
                }

            })
        }
        adapterHelper = QuickAdapterHelper.Builder(myAdapter).setTrailingLoadStateAdapter(loadAdapter).build()

        LogUtils.e(TAG, "onCreate path:${Environment.getExternalStorageDirectory()}")

//        val acMainRecyclerView = findViewById<RecyclerView>(R.id.acMainRecyclerView)
//        acMainRecyclerView.adapter = adapterHelper.adapter
//        acMainRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        LogUtils.i(TAG, "onRequestPermissionsResult requestCode:$requestCode permissions:${permissions.contentToString()} grantResults:${grantResults.contentToString()}")
    }


    @SuppressLint("CheckResult")
    fun loadFileBean(startIndex: Int, count: Int, block: (List<FileBean>) -> Unit) {
        LogUtils.i(TAG, "loadFileBean startIndex:$startIndex count:$count")
        Flowable.just(filePath).map {
            LogUtils.i(TAG, "map file:$it")
            val fileList = mutableListOf<FileBean>()
            val startTime = System.currentTimeMillis()
            val retriever = MediaMetadataRetriever()
            for (i in startIndex until startIndex + count) {
                retriever.setDataSource(it.absolutePath)
                var w = 0
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH)?.apply { w = toInt() }
                var h = 0
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT)?.apply { h = toInt() }
                var duration = 0
                retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.apply { duration = toInt() }
                fileList.add(FileBean(it, w, h, duration))
            }
            LogUtils.i(TAG, "end time:${System.currentTimeMillis() - startTime} fileList:${fileList.size}")
            fileList
        }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe({
            LogUtils.e(TAG, "onNext:${it.size}")
            block.invoke(it)
        }, {
            LogUtils.e(TAG, "onError:${it.message}")
            it.printStackTrace()
        })
    }
}


data class FileBean(val file: File, val width: Int, val height: Int, val duration: Int)

class MyAdapter : BaseQuickAdapter<FileBean, QuickViewHolder>() {

    override fun onBindViewHolder(holder: QuickViewHolder, position: Int, item: FileBean?) {
        item?.apply {
            val showText = "${position + 1}:${file.name}"
            holder.getView<TextView>(R.id.layoutItemTvMsg).text = showText
        }
    }

    override fun onCreateViewHolder(context: Context, parent: ViewGroup, viewType: Int): QuickViewHolder {
        return QuickViewHolder(R.layout.layout_item_lin, parent)
    }

}

/**
 * 自定义显示加载更多时的item View adapter
 */
internal class LoadStateAdapter(isLoadEndDisplay: Boolean = true) : TrailingLoadStateAdapter<LoadStateAdapter.TrailingLoadStateVH>(isLoadEndDisplay) {

    /**
     * Default implementation of "load more" ViewHolder
     *
     * 默认实现的"加载更多" ViewHolder
     */
    internal class TrailingLoadStateVH(
        parent: ViewGroup, view: View = LayoutInflater.from(parent.context).inflate(R.layout.brvah_trailing_load_more, parent, false)
    ) : RecyclerView.ViewHolder(view) {
        val loadCompleteView: View = itemView.findViewById(R.id.load_more_load_complete_view)
        val loadingView: View = itemView.findViewById(R.id.load_more_loading_view)
        val loadFailView: View = itemView.findViewById(R.id.load_more_load_fail_view)
        val loadEndView: View = itemView.findViewById(R.id.load_more_load_end_view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): TrailingLoadStateVH {
        return TrailingLoadStateVH(parent).apply {
            loadFailView.setOnClickListener {
                // 失败重试点击事件
                // retry when loaded failed.
                invokeFailRetry()
            }
            loadCompleteView.setOnClickListener {
                // 加载更多，手动点击事件
                // manual click to load more.
                invokeLoadMore()
            }
        }
    }

    /**
     * bind LoadState
     *
     * 绑定加载状态  这里处理各种加载状态的显示逻辑
     */
    override fun onBindViewHolder(holder: TrailingLoadStateVH, loadState: LoadState) {
        when (loadState) {
            is LoadState.NotLoading -> {
                if (loadState.endOfPaginationReached) {
                    holder.loadCompleteView.visibility = View.GONE
                    holder.loadingView.visibility = View.GONE
                    holder.loadFailView.visibility = View.GONE
                    holder.loadEndView.visibility = View.VISIBLE
                } else {
                    holder.loadCompleteView.visibility = View.VISIBLE
                    holder.loadingView.visibility = View.GONE
                    holder.loadFailView.visibility = View.GONE
                    holder.loadEndView.visibility = View.GONE
                }
            }

            is LoadState.Loading -> {
                holder.loadCompleteView.visibility = View.GONE
                holder.loadingView.visibility = View.VISIBLE
                holder.loadFailView.visibility = View.GONE
                holder.loadEndView.visibility = View.GONE
            }

            is LoadState.Error -> {
                holder.loadCompleteView.visibility = View.GONE
                holder.loadingView.visibility = View.GONE
                holder.loadFailView.visibility = View.VISIBLE
                holder.loadEndView.visibility = View.GONE
            }

            is LoadState.None -> {
                holder.loadCompleteView.visibility = View.GONE
                holder.loadingView.visibility = View.GONE
                holder.loadFailView.visibility = View.GONE
                holder.loadEndView.visibility = View.GONE
            }
        }
    }

    override fun getStateViewType(loadState: LoadState): Int = R.layout.brvah_trailing_load_more
}
