package cn.qatime.player.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.qatime.player.R;
import cn.qatime.player.adapter.CommonAdapter;
import cn.qatime.player.adapter.ViewHolder;
import cn.qatime.player.base.BaseFragment;
import cn.qatime.player.utils.StringUtils;
import cn.qatime.player.view.VerticalListView;

public class FragmentNEVideoPlayer33 extends BaseFragment {
    private List<String> lists = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_nevideo_player33, null);
        VerticalListView list = (VerticalListView) view.findViewById(R.id.list);
        lists.add("1");
        lists.add("1");
        lists.add("1");
        lists.add("1");
        lists.add("1");
        lists.add("1");
        lists.add("1");
        lists.add("1");
        lists.add("1");
        lists.add("1");
        lists.add("1");
        lists.add("1");
        lists.add("1");
        lists.add("1");
        list.setAdapter(new CommonAdapter<String>(getActivity(), lists, R.layout.item_fragment_nevideo_player33) {
            @Override
            public void convert(ViewHolder holder, String item, int position) {
                holder.setText(R.id.number, StringUtils.Int2String(position + 1));
            }
        });
        return view;
    }
}
