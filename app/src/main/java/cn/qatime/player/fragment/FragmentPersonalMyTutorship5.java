package cn.qatime.player.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.qatime.player.R;
import cn.qatime.player.activity.NEVideoPlayerActivity;
import cn.qatime.player.activity.OrderConfirmActivity;
import cn.qatime.player.activity.RemedialClassDetailActivity;
import cn.qatime.player.adapter.CommonAdapter;
import cn.qatime.player.adapter.ViewHolder;
import cn.qatime.player.base.BaseApplication;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.bean.OrderPayBean;
import cn.qatime.player.bean.RemedialClassBean;
import cn.qatime.player.bean.TutorialClassBean;
import cn.qatime.player.utils.DaYiJsonObjectRequest;
import cn.qatime.player.utils.JsonUtils;
import cn.qatime.player.utils.LogUtils;
import cn.qatime.player.utils.StringUtils;
import cn.qatime.player.utils.UrlUtils;
import cn.qatime.player.utils.VolleyErrorListener;
import cn.qatime.player.utils.VolleyListener;

public class FragmentPersonalMyTutorship5 extends BaseFragment {
    private PullToRefreshListView listView;
    private java.util.List<TutorialClassBean.Data> list = new ArrayList<>();
    private CommonAdapter<TutorialClassBean.Data> adapter;
    private int page = 1;

    private SimpleDateFormat parse = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personal_my_tutorship5, container, false);
        initview(view);
        return view;
    }

    private void initview(View view) {
        listView = (PullToRefreshListView) view.findViewById(R.id.list);
        listView.getRefreshableView().setDividerHeight(1);
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        listView.getLoadingLayoutProxy(true, false).setPullLabel(getResources().getString(R.string.pull_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setPullLabel(getResources().getString(R.string.pull_to_load));
        listView.getLoadingLayoutProxy(true, false).setRefreshingLabel(getResources().getString(R.string.refreshing));
        listView.getLoadingLayoutProxy(false, true).setRefreshingLabel(getResources().getString(R.string.loading));
        listView.getLoadingLayoutProxy(true, false).setReleaseLabel(getResources().getString(R.string.release_to_refresh));
        listView.getLoadingLayoutProxy(false, true).setReleaseLabel(getResources().getString(R.string.release_to_load));

        adapter = new CommonAdapter<TutorialClassBean.Data>(getActivity(), list, R.layout.item_fragment_personal_my_tutorship5) {
            @Override
            public void convert(ViewHolder helper, final TutorialClassBean.Data item, int position) {
                helper.setText(R.id.class_start_time, getResources().getString(R.string.item_class_start_date) + item.getLive_start_time());


                helper.setText(R.id.class_end_time, getResources().getString(R.string.item_class_end_date) + item.getLive_end_time());

                helper.getView(R.id.video).setVisibility(StringUtils.isNullOrBlanK(item.getPull_address()) ? View.GONE : View.VISIBLE);
                helper.getView(R.id.enter).setVisibility(item.getIs_bought() ? View.GONE : View.VISIBLE);
                helper.getView(R.id.video).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), NEVideoPlayerActivity.class);
                        intent.putExtra("url", item.getPull_address());
                        startActivity(intent);
                    }
                });
                helper.getView(R.id.enter).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(getActivity(), OrderConfirmActivity.class);
                        intent.putExtra("id", item.getId());
                        OrderPayBean bean = new OrderPayBean();
                        bean.image = item.getPublicize();
                        bean.name = item.getName();
                        bean.subject = item.getSubject();
                        bean.grade = item.getGrade();
                        bean.classnumber = item.getPreset_lesson_count();
                        bean.teacher = item.getTeacher().getName();
                        bean.classendtime = item.getLive_end_time();
                        bean.status = item.getStatus();
                        bean.classstarttime = item.getLive_start_time();
                        bean.price = item.getPrice();

                        intent.putExtra("data", bean);
                        startActivity(intent);
                    }
                });
                Glide.with(getActivity()).load(item.getPublicize()).placeholder(R.mipmap.photo).centerCrop().crossFade().into((ImageView) helper.getView(R.id.image));
                helper.setText(R.id.name, item.getName());
                helper.setText(R.id.subject, getResources().getString(R.string.item_subject)+ item.getSubject());
                helper.setText(R.id.teacher,getResources().getString(R.string.item_teacher)+ item.getTeacher_name());
                helper.setText(R.id.progress, item.getCompleted_lesson_count() + "/" + item.getPreset_lesson_count());
                ((ProgressBar) helper.getView(R.id.progressbar)).setProgress(item.getCompleted_lesson_count());
                ((ProgressBar) helper.getView(R.id.progressbar)).setMax(item.getPreset_lesson_count());
                helper.setText(R.id.remain_class, String.valueOf(item.getPreset_lesson_count()-item.getCompleted_lesson_count()));


                helper.setText(R.id.next_teaching_time, getResources().getString(R.string.item_next_class)+ item.getPreview_time());


            }
        };
        listView.setAdapter(adapter);

        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                page = 1;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        String label = DateUtils.formatDateTime(
                                getActivity(),
                                System.currentTimeMillis(),
                                DateUtils.FORMAT_SHOW_TIME
                                        | DateUtils.FORMAT_SHOW_DATE
                                        | DateUtils.FORMAT_ABBREV_ALL);
                        // Update the LastUpdatedLabel
                        listView.getLoadingLayoutProxy(false, true)
                                .setLastUpdatedLabel(label);
                        listView.onRefreshComplete();
                    }
                }, 200);
                initData(1);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        String label = DateUtils.formatDateTime(
                                getActivity(),
                                System.currentTimeMillis(),
                                DateUtils.FORMAT_SHOW_TIME
                                        | DateUtils.FORMAT_SHOW_DATE
                                        | DateUtils.FORMAT_ABBREV_ALL);
                        // Update the LastUpdatedLabel
                        listView.getLoadingLayoutProxy(false, true)
                                .setLastUpdatedLabel(label);
                        listView.onRefreshComplete();
                    }
                }, 200);
                initData(2);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), RemedialClassDetailActivity.class);
                intent.putExtra("id", list.get(position - 1).getId());
                startActivity(intent);
            }
        });
        listView.getRefreshableView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO  长按点击事件
                return true;
            }
        });
    }

    public void onShow() {
        if (!isLoad) {
            initData(1);
        }
    }

    /**
     * @param type 1刷新
     *             2加载更多
     */
    private void initData(final int type) {
        Map<String, String> map = new HashMap<>();
        map.put("page", String.valueOf(page));
        map.put("per_page", "10");
        map.put("cate", "taste");

        DaYiJsonObjectRequest request = new DaYiJsonObjectRequest(UrlUtils.getUrl(UrlUtils.urlMyRemedialClass + BaseApplication.getUserId() + "/courses", map), null,
                new VolleyListener(getActivity()) {

                    @Override
                    protected void onSuccess(JSONObject response) {
                        isLoad = true;
                        LogUtils.e(response.toString());
                        if (type == 1) {
                            list.clear();
                        }
                        String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
                        listView.getLoadingLayoutProxy(true, false).setLastUpdatedLabel(label);
                        listView.onRefreshComplete();

                        try {
                            TutorialClassBean data = JsonUtils.objectFromJson(response.toString(), TutorialClassBean.class);
                            if (data != null) {
                                list.addAll(data.getData());
                            }
                            adapter.notifyDataSetChanged();
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    protected void onError(JSONObject response) {

                    }

                    @Override
                    protected void onTokenOut() {
                        tokenOut();
                    }

                }, new VolleyErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
                listView.onRefreshComplete();
            }
        });
        addToRequestQueue(request);
    }
}
