package com.issac.novel

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.bifan.txtreaderlib.main.TxtReaderView

class TxtReaderView2 : TxtReaderView{


    private lateinit var mEdgePageTouchListen: EdgePageTouchListen
    private var mIsLoading: Boolean = false

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)


    override fun onActionMove(event: MotionEvent?) {

        when {
            (mIsLoading) -> return
            (event?.x!! > width/2 + 50) && isLastPage -> mEdgePageTouchListen.onTouch(false)
            (event.x < width/2 - 50) && isFirstPage -> mEdgePageTouchListen.onTouch(true)
            else -> super.onActionMove(event)
        }
    }

    override fun onActionUp(event: MotionEvent?) {
        if(mIsLoading)  return
        super.onActionUp(event)
    }

    fun setLoadingStatus(isLoading: Boolean){
        mIsLoading = isLoading
    }


    fun setEdgePageTouchListener(listen: EdgePageTouchListen){
        mEdgePageTouchListen = listen
    }

   interface EdgePageTouchListen{
       fun onTouch(pre: Boolean)
   }



}