package com.rs.mobile.wportal.fragment.xsgr;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.rs.mobile.common.AppConfig;
import com.rs.mobile.common.C;
import com.rs.mobile.common.S;
import com.rs.mobile.common.network.OkHttpHelper;
import com.rs.mobile.common.util.GsonUtils;
import com.rs.mobile.wportal.Constant;
import com.rs.mobile.wportal.R;
import com.rs.mobile.wportal.adapter.xsgr.OrderOneAdapter;
import com.rs.mobile.wportal.adapter.xsgr.OrderTwoAdapter;
import com.rs.mobile.wportal.entity.BaseEntity;
import com.rs.mobile.wportal.entity.MyShopInfoBean;
import com.rs.mobile.wportal.entity.OrderBean;
import com.rs.mobile.wportal.fragment.BaseFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Request;

public class MyOrderFragment2 extends BaseFragment {

    private int size = 5;
    private OrderBean bean;
    private OrderTwoAdapter adapter1;
    private View rootView;
    RecyclerView recyclerView;
    private List<OrderBean.DataBean> list;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int page = 0;
    Dialog mDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_my_order_done, container, false);
        list = new ArrayList<>();
        initView(rootView);
        isPrepared = true;
        lazyLoad();

        return rootView;
    }


    private void initdata() {
        initShopInfoData();
    }

    private void initView(View rootView) {


        View emptyView = LayoutInflater.from(getContext()).inflate(R.layout.layout_empty, null);
        emptyView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        //添加空视图


        recyclerView = (RecyclerView) rootView.findViewById(R.id.listview_order_new);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        adapter1 = new OrderTwoAdapter(getContext(), R.layout.item_order_two, list);
        adapter1.bindToRecyclerView(recyclerView);
        recyclerView.setAdapter(adapter1);
        adapter1.setEmptyView(emptyView);
        adapter1.disableLoadMoreIfNotFullPage();
        adapter1.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (swipeRefreshLayout.isRefreshing()){
                    return;
                }

                if (view.getId() == R.id.layout_open) {
                    if (adapter.getViewByPosition(recyclerView, position, R.id.layout_include).getVisibility() == View.VISIBLE) {
                        adapter.getViewByPosition(recyclerView, position, R.id.layout_include).setVisibility(View.GONE);
                        ((TextView) adapter.getViewByPosition(recyclerView, position, R.id.tv_order_new_pull)).setText(getResources().getString(R.string.orderopen));
                        ((ImageView) adapter.getViewByPosition(recyclerView, position, R.id.img_order_iocn)).setImageResource(R.drawable.icon_open_goods);
                    } else {
                        adapter.getViewByPosition(recyclerView, position, R.id.layout_include).setVisibility(View.VISIBLE);
                        ((TextView) adapter.getViewByPosition(recyclerView, position, R.id.tv_order_new_pull)).setText(getResources().getString(R.string.orderclose));
                        ((ImageView) adapter.getViewByPosition(recyclerView, position, R.id.img_order_iocn)).setImageResource(R.drawable.icon_close_goods);

                    }

                } else if (view.getId() == R.id.button_sure) {
                    showDialog(list.get(position).getOrder_num(), "31");
                } else if (view.getId() == R.id.button_cancel) {
                    showSelectDialog(list.get(position).getOrder_num(), list.get(position).getCustom_code());
                }
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_ly);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reinitdata();
            }
        });

        adapter1.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {

                initShopInfoData();
            }
        });
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (swipeRefreshLayout.isRefreshing()){
                    swipeRefreshLayout.setRefreshing(false);
                    return true;
                }
                return false;
            }
        });
    }


    public void initShopInfoData() {

        HashMap<String, String> param = new HashMap<String, String>();

        param.put("token", S.getShare(getContext(), C.KEY_JSON_TOKEN, ""));
        param.put("custom_code", S.getShare(getContext(), C.KEY_JSON_CUSTOM_CODE, ""));
        param.put("lang_type", AppConfig.LANG_TYPE);
//        param.put("custom_code", "01071390103abcde");
//        param.put("token", "186743935020f829f883e9fe-c8cf-4f60-9ed2-bd645cb1c118");
        param.put("pg", (page + 1) + "");
        param.put("pagesize", "" + size);
        param.put("orderclassify", "2");
        OkHttpHelper okHttpHelper = new OkHttpHelper(getContext());
        okHttpHelper.addSMPostRequest(new OkHttpHelper.CallbackLogic() {

            @Override
            public void onNetworkError(Request request, IOException e) {

            }

            @Override
            public void onBizSuccess(String responseDescription, JSONObject data, String flag) {


                bean = GsonUtils.changeGsonToBean(responseDescription, OrderBean.class);
                list.addAll(bean.getData());
                page = page + 1;
                adapter1.setNewData(list);
                adapter1.loadMoreComplete();
                if (bean.getData().size() < size) {
                    adapter1.loadMoreEnd();
                }
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }

            @Override
            public void onBizFailure(String responseDescription, JSONObject data, String flag) {
                // TODO Auto-generated method stub

            }
        }, Constant.XS_BASE_URL + "AppSM/requestNewOrderList", param);

    }

    public void reinitdata() {
        list.clear();
        page = 0;
        initShopInfoData();
    }

    public void changeOrderStatus(String ordernum, String status) {

        HashMap<String, String> param = new HashMap<String, String>();

        param.put("lang_type", AppConfig.LANG_TYPE);
        param.put("token", S.getShare(getContext(), C.KEY_JSON_TOKEN, ""));
        param.put("custom_code", S.getShare(getContext(), C.KEY_JSON_CUSTOM_CODE, ""));
//        param.put("custom_code", "01071390103abcde");
//        param.put("token", "186743935020f829f883e9fe-c8cf-4f60-9ed2-bd645cb1c118");
        param.put("order_num", ordernum);
        param.put("status", status);
        OkHttpHelper okHttpHelper = new OkHttpHelper(getContext());
        okHttpHelper.addSMPostRequest(new OkHttpHelper.CallbackLogic() {

            @Override
            public void onNetworkError(Request request, IOException e) {

            }

            @Override
            public void onBizSuccess(String responseDescription, JSONObject data, String flag) {

                try {
                    JSONObject jsonObject = new JSONObject(responseDescription);
                    if ("1".equals(jsonObject.getString("status"))) {
                        reinitdata();
                        mDialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onBizFailure(String responseDescription, JSONObject data, String flag) {

            }
        }, Constant.XS_BASE_URL + "AppSM/requestOrderTake", param);

    }

    private void showSelectDialog(final String ordernum, final String customcode) {
        new AlertDialog.Builder(getContext())
                .setTitle(getResources().getString(R.string.quxiaoqueren))
                .setMessage(getResources().getString(R.string.quxiaoquerenneirong))
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(getResources().getString(R.string.sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancelOrderStatus(ordernum, customcode);
                    }
                })
                .create().show();


    }

    protected void showDialog(final String ordernum, final String status) {
        mDialog = new Dialog(getContext());

        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.layout_sureorder, null);


        Button okbt = (Button) dialogView.findViewById(R.id.button_ok);
        okbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeOrderStatus(ordernum, status);
            }
        });

        mDialog.setContentView(dialogView);
        mDialog.show();

    }

    public void cancelOrderStatus(String ordernum, final String customer_code) {

        HashMap<String, String> param = new HashMap<String, String>();

        param.put("lang_type", AppConfig.LANG_TYPE);
        param.put("token", S.getShare(getContext(), C.KEY_JSON_TOKEN, ""));
        param.put("custom_code", S.getShare(getContext(), C.KEY_JSON_CUSTOM_CODE, ""));
//        param.put("custom_code", "01071390103abcde");
//        param.put("token", "186743935020f829f883e9fe-c8cf-4f60-9ed2-bd645cb1c118");
        param.put("order_num", ordernum);
        param.put("customer_code", customer_code);
        OkHttpHelper okHttpHelper = new OkHttpHelper(getContext());
        okHttpHelper.addSMPostRequest(new OkHttpHelper.CallbackLogic() {

            @Override
            public void onNetworkError(Request request, IOException e) {

            }

            @Override
            public void onBizSuccess(String responseDescription, JSONObject data, String flag) {


                try {
                    JSONObject jsonObject = new JSONObject(responseDescription);
                    if ("1".equals(jsonObject.getString("status"))) {
                        reinitdata();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onBizFailure(String responseDescription, JSONObject data, String flag) {

            }
        }, Constant.XS_BASE_URL + "AppSM/requestOrderCancel", param);

    }


    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible) {
            return;
        }
        list.clear();
        adapter1.setNewData(list);
        page = 0;
        initdata();
    }
}
