package pl.olszak.michal.beaconscanner

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * @author molszak
 *         created on 22.01.2018.
 */
class LinearSpacingItemDecoration : RecyclerView.ItemDecoration(){

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView?, state: RecyclerView.State?) {
        outRect.left = DEFAULT_SPACING
        outRect.right = DEFAULT_SPACING
        outRect.top = DEFAULT_SPACING
        outRect.bottom = DEFAULT_SPACING
    }

    companion object {
        const val DEFAULT_SPACING = 12

    }
}