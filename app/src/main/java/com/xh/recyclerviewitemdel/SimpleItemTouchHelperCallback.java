package com.xh.recyclerviewitemdel;

import android.graphics.Canvas;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;

/**
 * Created by snaillove on 2016/4/6.
 */
public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback{

    private onMoveAndSwipedListener listener ;
    private float ALPHA_FULL = 1f ;
    public SimpleItemTouchHelperCallback(onMoveAndSwipedListener listener) {
        this.listener = listener;
    }

    /**这个方法是用来设置我们拖动的方法以及侧滑的方向*/
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        //如果是GridView样式的RecyclerView
        if(recyclerView.getLayoutManager() instanceof GridLayoutManager){
            //设置拖拽方向为 上下左右
            final int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN |ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT ;
            //不支持侧滑
            final int swipeFlags = 0 ;
            return  makeMovementFlags(dragFlags,swipeFlags);
        }else {//如果ListView样式的RecyclerView
            //设置拖拽方向为上下
            final int dragFlags = ItemTouchHelper.UP|ItemTouchHelper.DOWN ;
            //设置侧滑方向为 左右和右左都可以
            final int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END ;
            //将方向参数设置进去
            return makeMovementFlags(dragFlags , swipeFlags);
        }
    }

    /**当我们拖动Item时会回调此方法*/
    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
       //如果两个Item 不是一个类型，我们让他不可以拖拽
        if(viewHolder.getItemViewType() != target.getItemViewType()){
            return false ;
        }
        //回调listener中的onItemMove方法
        listener.onItemMove(viewHolder.getAdapterPosition(),target.getAdapterPosition());
        return true;
    }

    /**当我们侧滑Item时会回调此方法*/
    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //回调listener 中的onItemDismiss方法
        listener.onItemDismiss(viewHolder.getAdapterPosition());
    }

    /**当状态改变时回调该方法*/
    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        //当前不是idel（空闲） 状态时 ；说明当前正在被 拖拽或者侧滑
        if(actionState != ItemTouchHelper.ACTION_STATE_IDLE){
            //看看这个ViewHolder是否实现了onStateChangeListener 接口
//            Log.e("TAG", "--- onSelectedChanged");
            if(viewHolder instanceof onStateChangeListener){
                onStateChangeListener tempListener = (onStateChangeListener) viewHolder;
                //回调ItemViewHolder中的onItemSelected方法来改变item的背景颜色
                    tempListener.onItemSelect();
            }


        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    /**当用户拖拽完或者侧滑完一个item时回调此方法，用来清除施加在item上的一些状态*/
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        if(viewHolder instanceof onStateChangeListener){
            onStateChangeListener listener = (onStateChangeListener)viewHolder;
            listener.onItemClear();
        }
    }

    /**当item被拖拽或者侧滑的时候会回调onMove和onSwiped方法，所以我们需要同时Adapter做出相应的改变，
     * 对mItems数据做出交换或者删除的操作，因此我们需要一个回调接口来继续回调Adapter中的方法*/
    public interface onMoveAndSwipedListener{
        boolean onItemMove(int fromPositiom,int toPosition);
        void onItemDismiss(int position);
    }
    /**这个方法可以判断当前是拖拽还是侧滑*/
    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
            //根据侧滑的位移来修改item的透明度
            final float alpha = ALPHA_FULL - Math.abs(dX) / (float) viewHolder.itemView.getWidth();
            viewHolder.itemView.setAlpha(alpha);
            viewHolder.itemView.setTranslationX(dX);
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    //item 右侧imageView 一按下触发  ItemTouchHelper.startDrag(viewHolder) 方法所用到的回调;
    public interface onStartDragListener{
        void startDrag(RecyclerView.ViewHolder viewHolder);
    }
    //改变item的背景颜色我们仍然需要在adapter中去做实际的修改，因此我们还需要一个回调接口，
    public interface onStateChangeListener{
        void onItemSelect();
        void onItemClear();
    }

}
