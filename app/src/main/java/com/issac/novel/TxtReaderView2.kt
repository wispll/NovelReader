package com.issac.novel

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import com.bifan.txtreaderlib.main.TxtReaderView

class TxtReaderView2 : TxtReaderView{


    private lateinit var mEdgePageFlingCallback: EdgePageFlingCallback

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)


    fun setEdgePageFlingCallback(callback: EdgePageFlingCallback){
        mEdgePageFlingCallback = callback
    }


    @Override
    override fun onFling(
        e1: MotionEvent,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        val maxVelocityX = 1000f
        if (CurrentMode == Mode.Normal) {//正常情况下快速滑动，执行翻页动作
            if (isPagePre && velocityX > maxVelocityX) {

                return when(isFirstPage){
                    false -> {
                        startPagePreAnimation()
                        true
                    }
                    true -> {
                        mEdgePageFlingCallback(true)
                        false
                    }
                }
            } else if (isPageNext && velocityX < -maxVelocityX) {
                return when(isLastPage){
                    false -> {
                        startPageNextAnimation()
                        true
                    }
                    true -> {
                        mEdgePageFlingCallback(false)
                        false
                    }
                }
            }
        }
        return false
    }
}

typealias EdgePageFlingCallback = (firstPage: Boolean) -> Unit
