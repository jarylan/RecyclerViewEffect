package com.xh.recyclerviewitemdel;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by snaillove on 2016/3/28.
 */
public class RecyclerListFragment extends Fragment implements SimpleItemTouchHelperCallback.onStartDragListener{

    private ItemTouchHelper mItemTouchHelper;

    public RecyclerListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return new RecyclerView(container.getContext());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(getActivity(),this);
        //参数view即为我们在onCreateView中return 的view ；
        RecyclerView recyclerView = (RecyclerView) view;
        //固定recyclerView 的大小；
        recyclerView.setHasFixedSize(true);
        //设置adapter
        recyclerView.setAdapter(adapter);
        //设置布局类型 为LinearLayoutManager,相当于ListView的样式 ；
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        //关联ItemTouchHelper和 RecyclerView
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
         mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(recyclerView);

    }


    @Override
    public void startDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
}
