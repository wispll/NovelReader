package com.issac.novel.fragment

import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.bifan.txtreaderlib.bean.TxtMsg
import com.bifan.txtreaderlib.interfaces.ICenterAreaClickListener
import com.bifan.txtreaderlib.interfaces.ILoadListener
import com.bifan.txtreaderlib.main.TxtConfig
import com.blankj.utilcode.util.ToastUtils
import com.github.ybq.android.spinkit.SpinKitView
import com.issac.novel.MainActivity
import com.issac.novel.R
import com.issac.novel.TxtReaderView2
import com.issac.novel.db.NovelHistory
import com.issac.novel.extract.Content
import com.issac.novel.model.ContentModel
import com.issac.novel.model.HistoryModel
import kotlinx.android.parcel.Parcelize
import timber.log.Timber
import java.util.*

open class ReaderFragment: Fragment() {

    private lateinit  var mTxtReaderView: TxtReaderView2
    private lateinit var mTopMenu: View
    private lateinit var mBottomMenu: View
    private lateinit var mMenuHolder: MenuHolder
    private lateinit var mSpinKit: SpinKitView

    lateinit var mCurrentContent: Content
    lateinit var mArticleTitle: String
    lateinit var mAuthor: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_reader, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init(view)
        initWhenLoadDone()
        registerListener()

        val parcelable = arguments?.getParcelable<Arguments>("arg")
        mArticleTitle = parcelable?.article_title.toString()
        mAuthor = parcelable?.author.toString()

        fetchContent(parcelable?.href!!)
//        fetchContent("book/31583/404388.html")
    }

    override fun onDestroy() {
        super.onDestroy()
        clearReaderContext()
    }


    private fun init(view: View) {
        mTxtReaderView = view.findViewById(R.id.activity_hwtxtplay_readerView)!!
        mTopMenu = view.findViewById(R.id.activity_hwtxtplay_menu_top)!!
        mBottomMenu = view.findViewById(R.id.activity_hwtxtplay_menu_bottom)
        mSpinKit = view.findViewById(R.id.spin_kit)

        mMenuHolder = MenuHolder(
            view.findViewById(R.id.back_image),
            view.findViewById(R.id.txtreadr_menu_title),
            view.findViewById(R.id.txtreadr_menu_chapter_pre),
            view.findViewById(R.id.txtreadr_menu_chapter_next),
            view.findViewById(R.id.txtreadr_menu_seekbar),
            view.findViewById(R.id.txtreadr_menu_textsize_del),
            view.findViewById(R.id.txtreadr_menu_textsize_add),
            view.findViewById(R.id.txtreadr_menu_textsize),
            view.findViewById(R.id.txtreadr_menu_textsetting1_bold),
            view.findViewById(R.id.txtreadr_menu_textsetting1_normal),
            view.findViewById(R.id.txtreadr_menu_textsetting2_cover),
            view.findViewById(R.id.txtreadr_menu_textsetting2_translate),
            view.findViewById(R.id.hwtxtreader_menu_style1),
            view.findViewById(R.id.hwtxtreader_menu_style2),
            view.findViewById(R.id.hwtxtreader_menu_style3),
            view.findViewById(R.id.hwtxtreader_menu_style4),
            view.findViewById(R.id.hwtxtreader_menu_style5)
        )

        mMenuHolder.mBackBt.setOnClickListener{
            val activity = requireContext() as MainActivity
            activity.supportFragmentManager.popBackStack()
        }
    }

    private val StyleTextColors = intArrayOf(
        Color.parseColor("#4a453a"),
        Color.parseColor("#505550"),
        Color.parseColor("#453e33"),
        Color.parseColor("#8f8e88"),
        Color.parseColor("#27576c")
    )


    private fun initWhenLoadDone() {
        TxtConfig.saveIsOnVerticalPageMode(requireContext(),false)
        mMenuHolder.mTextSize.text = mTxtReaderView.textSize.toString()
        //mTxtReaderView.setLeftSlider(new MuiLeftSlider());//修改左滑动条
        //mTxtReaderView.setRightSlider(new MuiRightSlider());//修改右滑动条
        //字体初始化
        onTextSettingUi(mTxtReaderView.txtReaderContext.txtConfig.Bold)
        //翻页初始化
        onPageSwitchSettingUi(mTxtReaderView.txtReaderContext.txtConfig.SwitchByTranslate)
        //保存的翻页模式
        if (mTxtReaderView.txtReaderContext.txtConfig.SwitchByTranslate) {
            mTxtReaderView.setPageSwitchByTranslate()
        } else {
            mTxtReaderView.setPageSwitchByCover()
        }
    }

    private fun registerListener() {
        setMenuListener()
        setSeekBarListener()
        setPageChangeListener()
        setCenterClickListener()
        setStyleChangeListener()
        setExtraListener()
        setOnEdgePageTouchListener()
    }

    private fun setOnEdgePageTouchListener() {
        mTxtReaderView.setEdgePageTouchListener(object: TxtReaderView2.EdgePageTouchListen{
            override fun onTouch(pre: Boolean) {
                when(pre){
                    true->  mMenuHolder.mPreChapter.performClick()
                    false-> mMenuHolder.mNextChapter.performClick()
                }
            }
        })
    }

    private fun setExtraListener() {
        mMenuHolder.mPreChapter.setOnClickListener(PreChapterListener())
        mMenuHolder.mNextChapter.setOnClickListener(NextChapterListener())
        mMenuHolder.mTextSizeAdd.setOnClickListener(TextChangeClickListener(true))
        mMenuHolder.mTextSizeDel.setOnClickListener(TextChangeClickListener(false))
        mMenuHolder.mBoldSelectedLayout.setOnClickListener(TextSettingClickListener(true))
        mMenuHolder.mNormalSelectedLayout.setOnClickListener(TextSettingClickListener(false))
        mMenuHolder.mTranslateSelectedLayout.setOnClickListener(SwitchSettingClickListener(true))
        mMenuHolder.mCoverSelectedLayout.setOnClickListener(SwitchSettingClickListener(false))
    }

    private fun setStyleChangeListener() {
        mMenuHolder.mStyle1.setOnClickListener(
            StyleChangeClickListener(
                ContextCompat.getColor(
                    this.requireContext(),
                    R.color.hwtxtreader_styleclor1
                ), StyleTextColors[0]
            )
        )
        mMenuHolder.mStyle2.setOnClickListener(
            StyleChangeClickListener(
                ContextCompat.getColor(
                    this.requireContext(),
                    R.color.hwtxtreader_styleclor2
                ), StyleTextColors[1]
            )
        )
        mMenuHolder.mStyle3.setOnClickListener(
            StyleChangeClickListener(
                ContextCompat.getColor(
                    this.requireContext(),
                    R.color.hwtxtreader_styleclor3
                ), StyleTextColors[2]
            )
        )
        mMenuHolder.mStyle4.setOnClickListener(
            StyleChangeClickListener(
                ContextCompat.getColor(
                    this.requireContext(),
                    R.color.hwtxtreader_styleclor4
                ), StyleTextColors[3]
            )
        )
        mMenuHolder.mStyle5.setOnClickListener(
            StyleChangeClickListener(
                ContextCompat.getColor(
                    this.requireContext(),
                    R.color.hwtxtreader_styleclor5
                ), StyleTextColors[4]
            )
        )
    }


    private fun setPageChangeListener() {
        mTxtReaderView.setPageChangeListener { progress ->
            mMenuHolder.mSeekBar.progress = (progress * 100).toInt()
        }
    }


    private fun setMenuListener() {
        mTopMenu.setOnTouchListener { view, motionEvent -> true }
        mBottomMenu.setOnTouchListener { view, motionEvent -> true }
    }

    private fun onTextSettingUi(isBold: Boolean) {
        if (isBold) {
            mMenuHolder.mBoldSelectedLayout.setBackgroundResource(R.drawable.shape_menu_textsetting_selected)
            mMenuHolder.mNormalSelectedLayout.setBackgroundResource(R.drawable.shape_menu_textsetting_unselected)
        } else {
            mMenuHolder.mBoldSelectedLayout.setBackgroundResource(R.drawable.shape_menu_textsetting_unselected)
            mMenuHolder.mNormalSelectedLayout.setBackgroundResource(R.drawable.shape_menu_textsetting_selected)
        }
    }

    private fun onPageSwitchSettingUi(isTranslate: Boolean) {
        if (isTranslate) {
            mMenuHolder.mTranslateSelectedLayout.setBackgroundResource(R.drawable.shape_menu_textsetting_selected)
            mMenuHolder.mCoverSelectedLayout.setBackgroundResource(R.drawable.shape_menu_textsetting_unselected)
        } else {
            mMenuHolder.mTranslateSelectedLayout.setBackgroundResource(R.drawable.shape_menu_textsetting_unselected)
            mMenuHolder.mCoverSelectedLayout.setBackgroundResource(R.drawable.shape_menu_textsetting_selected)
        }
    }

    private fun Show(vararg views: View) {
        for (v in views) {
            v.visibility = View.VISIBLE
        }
    }

    private fun Gone(vararg views: View) {
        for (v in views) {
            v.visibility = View.GONE
        }
    }


    private inner class MenuHolder(
        var mBackBt: ImageView,
        var mTitle: TextView,
        var mPreChapter: TextView,
        var mNextChapter: TextView,
        var mSeekBar: SeekBar,
        var mTextSizeDel: View,
        var mTextSizeAdd: View,
        var mTextSize: TextView,
        var mBoldSelectedLayout: View,
        var mNormalSelectedLayout: View,
        var mCoverSelectedLayout: View,
        var mTranslateSelectedLayout: View,
        var mStyle1: View,
        var mStyle2: View,
        var mStyle3: View,
        var mStyle4: View,
        var mStyle5: View
    )

    protected fun setBookName(name: String) {
        mMenuHolder.mTitle.text = name
    }

    private inner class PreChapterListener : View.OnClickListener {

        override fun onClick(view: View) {
            val path = mCurrentContent.last_chapter_href
            if(path!= null){
                fetchContent(path)
            }else{
                ToastUtils.showLong(R.string.first_chapter)
            }
        }
    }

    private inner class NextChapterListener : View.OnClickListener {

        override fun onClick(view: View) {
            val path = mCurrentContent.next_chapter_href
            if(path!= null){
                fetchContent(path)
            }else{
                ToastUtils.showLong(R.string.last_chapter)
            }
        }
    }


    private fun setSeekBarListener() {

        mMenuHolder.mSeekBar.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                mTxtReaderView.loadFromProgress(mMenuHolder.mSeekBar.progress.toFloat())
            }
            false
        }
    }

    private fun setCenterClickListener() {
        mTxtReaderView.setOnCenterAreaClickListener(object : ICenterAreaClickListener {
            override fun onCenterClick(widthPercentInView: Float): Boolean {
                if(mTopMenu.isVisible || mBottomMenu.isVisible){
                    Gone(mTopMenu, mBottomMenu)
                }else{
                    Show(mTopMenu, mBottomMenu)
                }
                return true
            }

            override fun onOutSideCenterClick(widthPercentInView: Float): Boolean {
                return false
            }
        })
    }

    private inner class TextChangeClickListener internal constructor(private val Add: Boolean?) :
        View.OnClickListener {

        override fun onClick(view: View) {
            val textSize = mTxtReaderView.textSize
            if (Add!!) {
                if (textSize + 2 <= TxtConfig.MAX_TEXT_SIZE) {
                    mTxtReaderView.textSize = textSize + 2
                    mMenuHolder.mTextSize.text = (textSize + 2).toString()
                }
            } else {
                if (textSize - 2 >= TxtConfig.MIN_TEXT_SIZE) {
                    mTxtReaderView.textSize = textSize - 2
                    mMenuHolder.mTextSize.text = (textSize - 2).toString()
                }
            }
        }
    }

    private inner class TextSettingClickListener internal constructor(private val Bold: Boolean?) :
        View.OnClickListener {

        override fun onClick(view: View) {
            mTxtReaderView.setTextBold(Bold!!)
            onTextSettingUi(Bold)
        }
    }

    private inner class StyleChangeClickListener internal constructor(
        private val BgColor: Int,
        private val TextColor: Int
    ) : View.OnClickListener {

        override fun onClick(view: View) {
            mTxtReaderView.setStyle(BgColor, TextColor)
        }
    }

    private inner class SwitchSettingClickListener(private val isSwitchTranslate: Boolean?) :
        View.OnClickListener {

        override fun onClick(view: View) {
            if (!isSwitchTranslate!!) {
                mTxtReaderView.setPageSwitchByCover()
            } else {
                mTxtReaderView.setPageSwitchByTranslate()
            }
            onPageSwitchSettingUi(isSwitchTranslate)
        }
    }

    private fun clearReaderContext() {
        mTxtReaderView.txtReaderContext.Clear()
    }







    var mIsLoading: Boolean = false

    private fun fetchContent(path: String){
        synchronized(mIsLoading){
            if(mIsLoading) {
                ToastUtils.showLong(R.string.loading_chapter)
                return
            }
            mIsLoading = true
            mTxtReaderView.setLoadingStatus(true)
        }

        mSpinKit.visibility = View.VISIBLE
        try{
            val contentModel = ContentModel()
            val liveData = contentModel.getLiveData(path)

            liveData?.observe(viewLifecycleOwner, Observer {
                mCurrentContent = it

                mTxtReaderView.txtReaderContext.pageData.refreshTag[0] =1
                mTxtReaderView.txtReaderContext.pageData.refreshTag[1] =1
                mTxtReaderView.txtReaderContext.pageData.refreshTag[2] =1

                mTxtReaderView.loadText(it.content, object : ILoadListener {
                    override fun onFail(txtMsg: TxtMsg?) {
                    }

                    override fun onMessage(message: String?) {
                    }

                    override fun onSuccess() {
                        setBookName(it.title)
                        mSpinKit.visibility = View.GONE
                        mIsLoading = false
                        mTxtReaderView.setLoadingStatus(false)

                        saveReadHistory(requireActivity(), NovelHistory(
                            UUID.nameUUIDFromBytes(mArticleTitle.toByteArray()).toString(),
                            mArticleTitle,
                            mAuthor,
                            mCurrentContent.title,
                            path,
                            System.currentTimeMillis()
                        ))
                    }
                })
            })
        }catch (e: Exception){
            Timber.e(e)
            ToastUtils.showLong(e.message)
            mIsLoading = false
            mTxtReaderView.setLoadingStatus(false)
        }

    }

    fun saveReadHistory(viewModelStoreOwner: ViewModelStoreOwner, history: NovelHistory){
        val model= ViewModelProvider(viewModelStoreOwner).get(HistoryModel::class.java)
        model.save2Db(history)
    }
}

@Parcelize
class Arguments(
    val href: String,
    val article_title: String,
    val author: String
): Parcelable


