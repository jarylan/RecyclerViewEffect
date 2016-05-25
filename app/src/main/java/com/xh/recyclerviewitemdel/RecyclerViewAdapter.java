package com.xh.recyclerviewitemdel;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by snaillove on 2016/3/29.
 */
class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ItemViewHolder> implements SimpleItemTouchHelperCallback.onMoveAndSwipedListener{

    public static final int items = 0;
    private List<String> mItems = new ArrayList<>();
    private SimpleItemTouchHelperCallback.onStartDragListener mStartDragListener;
    public MyRecyclerViewAdapter(Context context,SimpleItemTouchHelperCallback.onStartDragListener startDragListener) {
        //初始化数据
        mItems.addAll(Arrays.asList(context.getResources().getStringArray(R.array.dummy_items)));
        this.mStartDragListener = startDragListener;
    }

    /**在这里反射出我们的item的布局*/
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //利用反射将Item布局加载出来 ；
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,null);
        //new 一个我们的ViewHolder ,findViewById操作都在ItemViewHolder的构造方法中进行
        return new ItemViewHolder(view);
    }

    /**在这里为布局中的控件设置数据*/
    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        holder.text.setText(mItems.get(position));
        //holder是我们拖动item是要用的；
        holder.handle.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                /**在onTouch方法中，我们应该回调RecyclerListFragment类中的mItemTouchHelper，
                 * 调用mItemTouchHelper的onStartDrag方法，因此我们又需要一个回调接口*/
                //如果按下
                if(MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN){
                    //回调RecyclerListFragment 中的startDrag 方法
                    //让mItemTouchHelper 执行拖拽操作
                    mStartDragListener.startDrag(holder);
                }

                return false;
            }
        });
    }

    /**返回数据个数*/
    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder implements SimpleItemTouchHelperCallback.onStateChangeListener{
        private TextView text;
        private ImageView handle;
        public ItemViewHolder(View itemView) {
            super(itemView);
            text = (TextView) itemView.findViewById(R.id.text);
            handle = (ImageView) itemView.findViewById(R.id.handle);
        }

        @Override
        public void onItemSelect() {
            //设置item背景色
//            Log.e("TAG","--- Change itemView Color");
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            //恢复item的背景颜色
            itemView.setBackgroundColor(0);
        }
    }



    @Override
    public boolean onItemMove(int fromPositiom, int toPosition) {
        //交换mItems数据的位置；
        Collections.swap(mItems, fromPositiom, toPosition);
        //交换RecyclerView列表中item的位置
        notifyItemMoved(fromPositiom,toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        //删除mItems数据
        mItems.remove(position);
        //删除RecyclerView列表对应item
        notifyItemRemoved(position);
    }



}
