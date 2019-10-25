package com.xh.common.base


import android.content.Context


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.xh.common.R
import com.xh.common.ktextended.hide
import com.xh.common.ktextended.show


open class BaseAdapter<T> constructor(private val layoutId: Int,
                                      protected val mContext: Context,
                                      private var isOpenAddMore: Boolean = true)
    : RecyclerView.Adapter<ViewHolder>() {

    protected val inflater: LayoutInflater
    val mData: MutableList<T>

    private var isHasMore = true
    private var isAddHead: Boolean = false
    private var mHeadResId: Int = 0

    private var mLoadMoreListener: (() -> Unit)? = null

    private var animation: Animation? = null
    private var ivLoading: ImageView? = null
    private var tvMore: TextView? = null

//    private var listener: OnItemClickListener<T>? = null

    private var mItemClickListener: ((view: View, postion: Int, data: T) -> Unit)? = null

    private var isOpenNullLayout: Boolean = false
    private var mNullLayoutResId: Int = 0


    init {
        inflater = LayoutInflater.from(mContext)
        mData = mutableListOf()
    }

    fun addHeadView(@LayoutRes resId: Int) {
        isAddHead = true
        mHeadResId = resId
    }

    fun addNullView(@LayoutRes resId: Int) {
        isOpenNullLayout = true
        mNullLayoutResId = resId
    }

    override fun getItemCount(): Int {
        if (isOpenNullLayout && mData.size == 0) {
            return 1
        }

        var count = mData.size
        if (isOpenAddMore) {
            count++
        }
        if (isAddHead) {
            count++
        }
        return count
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var viewHolder: ViewHolder?
        var view: View? = null
        when (viewType) {
            LAYOUT_ORDINARY -> view = inflater.inflate(layoutId, parent, false)
            LAYOUT_ADD_MORE -> view = inflater.inflate(R.layout.item_add_more, parent, false)
            LAYOUT_HEAD -> view = inflater.inflate(mHeadResId, parent, false)
            LAYOUT_NULL -> view = inflater.inflate(mNullLayoutResId, parent, false)
        }
        if (view == null) {
            viewHolder = onMyCreateOtherViewHolder(parent, viewType)
        } else {
            viewHolder = ViewHolder(view)
        }

        return viewHolder!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        when (getItemViewType(position)) {
            LAYOUT_ORDINARY -> {
                val myPosition: Int
                if (isAddHead) {
                    myPosition = position - 1
                } else {
                    myPosition = position
                }
                onMyBindViewHolder(holder, position, getItemData(myPosition))
                holder.convertView.setOnClickListener { v ->
                    mItemClickListener?.invoke(v, position, mData[myPosition])

                }
            }
            LAYOUT_ADD_MORE -> {
                initAddMoreLayout(holder)
            }
            LAYOUT_HEAD -> {
                onMyBindHeadViewHolder(holder)
            }
            LAYOUT_NULL -> {

            }
            else -> {
                onMyBindOtherViewHolder(holder, position, getItemData(if (isAddHead) position - 1 else position))
            }
        }

    }


    override fun getItemViewType(position: Int): Int {
        if (isOpenNullLayout && mData.size == 0) {
            return LAYOUT_NULL
        }
        if (isAddHead && position == 0) {
            return LAYOUT_HEAD
        }
        if (isOpenAddMore && position == itemCount - 1) {
            return LAYOUT_ADD_MORE
        }
        return LAYOUT_ORDINARY
    }


    private fun initAddMoreLayout(viewHolder: ViewHolder) {
        if (ivLoading == null) {
            ivLoading = viewHolder.getView<ImageView>(R.id.iv_loading)
        }
        if (tvMore == null) {
            tvMore = viewHolder.getView<TextView>(R.id.tv_more)
        }
        if (isHasMore && mData.size != 0) {
            viewHolder.convertView.show()
            if (animation == null) {
                animation = RotateAnimation(0f, 359f, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f)
                animation!!.duration = 500
                animation!!.interpolator = LinearInterpolator()
                animation!!.repeatCount = -1
            }

            ivLoading!!.show()
            ivLoading!!.animation = animation
            tvMore!!.text = "加载更多..."
            mLoadMoreListener?.invoke()

        } else if (!isHasMore && mData.size > 0) {

            viewHolder.convertView.show()
            ivLoading!!.hide()
            tvMore!!.text = "没有更多了"

        } else {
            viewHolder.convertView.hide()
        }
    }

//    fun setOnItemClickListener(listener: OnItemClickListener<*>) {
//        this.listener = listener
//    }

    fun setHasMore(hasMore: Boolean) {
        isHasMore = hasMore
        if (!hasMore) {
            ivLoading?.hide()
            tvMore?.text = "没有更多了"
        }
        stopAnimation()
    }


    fun stopAnimation() {
        ivLoading?.clearAnimation()

    }


    //重置数据
    fun resetData(dataList: List<T>) {
        mData.clear()
        addAllData(dataList)
        notifyDataSetChanged()
    }

    fun addAllData(dataList: List<T>) {

        mData.addAll(dataList)
        notifyDataSetChanged()
//        notifyItemRangeChanged(mData.size, dataList.size)
    }

    fun addData(data: T) {
        mData.add(data)
        notifyItemChanged(mData.size)

    }

    fun addData(position: Int, data: T) {
        mData.add(position, data)
        notifyItemInserted(position)
    }

    fun replaceData(position: Int, data: T) {
        mData[position] = data
        notifyItemChanged(position)
    }

    fun updateData(position: Int) {
        if (itemCount > position) {
            notifyItemChanged(position)
        }
    }


    fun removeData(data: T) {
        if (mData.contains(data)) {
            val position = mData.indexOf(data)
            this.mData.remove(data)
            notifyItemRemoved(position)
        }
    }

    fun removeData(position: Int) {
        if (this.itemCount > position) {
            this.mData.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    fun getItemData(position: Int): T {
        return mData[position]
    }

    fun setLoadMoreListener(listener: () -> Unit) {
        mLoadMoreListener = listener
    }

    fun setOnItemClickListener(listener: (view: View, postion: Int, data: T) -> Unit) {
        mItemClickListener = listener
    }


//    protected abstract val layoutId: Int

    open protected fun onMyBindViewHolder(holder: ViewHolder, position: Int, data: T) {

    }

    open protected fun onMyBindHeadViewHolder(holder: ViewHolder) {

    }

    open protected fun onMyCreateOtherViewHolder(parent: ViewGroup, viewType: Int): ViewHolder? {
        return null
    }

    open protected fun onMyBindOtherViewHolder(holder: ViewHolder, position: Int, data: T) {

    }


    companion object {
        val LAYOUT_ORDINARY = 1
        val LAYOUT_ADD_MORE = 2
        val LAYOUT_HEAD = 3
        val LAYOUT_NULL = 4
    }

}
