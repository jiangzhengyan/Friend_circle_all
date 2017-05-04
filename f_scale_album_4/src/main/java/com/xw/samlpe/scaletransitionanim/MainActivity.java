package com.xw.samlpe.scaletransitionanim;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static  List<String> sUrls = new ArrayList<>();

    private static final String TAG = "MainActivity";

    static {
        sUrls.clear();

        Log.e(TAG, "static initializer: ");
        sUrls.add("http://photocdn.sohu.com/20160307/mp62252655_1457334772519_2.png");
        sUrls.add("http://img0.imgtn.bdimg.com/it/u=556618733,1205300389&fm=21&gp=0.jpg");
        sUrls.add("http://photocdn.sohu.com/20160307/mp62252655_1457334772519_2.png");
        sUrls.add("http://img1.imgtn.bdimg.com/it/u=3272030875,860665188&fm=21&gp=0.jpg");
        sUrls.add("http://img1.imgtn.bdimg.com/it/u=2237658959,3726297486&fm=21&gp=0.jpg");
        sUrls.add("http://img1.imgtn.bdimg.com/it/u=3016675040,1510439865&fm=21&gp=0.jpg");

        sUrls.add("http://imgcdn.thecover.cn/FlwmxD5lj9aZuAwIfNlTGeg4fbA6?imageMogr2/quality/80/ignore-error/1");
        sUrls.add("http://i0.itc.cn/20110124/8f7_98cce95e_ae0a_4712_8e65_1789fba80843_0.jpg");
        sUrls.add("http://simg314.magcasa.com/content_images/2015/10/22/162537/1445450806_8832.jpg");

        sUrls.add("http://img4.duitang.com/uploads/item/201307/02/20130702113059_UEGL2.jpeg");
        sUrls.add("http://img0.imgtn.bdimg.com/it/u=985035006,79865976&fm=21&gp=0.jpg");
        sUrls.add("http://img5.imgtn.bdimg.com/it/u=1774291582,2563335167&fm=21&gp=0.jpg");
        sUrls.add("http://photocdn.sohu.com/20160307/mp62252655_1457334772519_2.png");
        sUrls.add("http://img5.imgtn.bdimg.com/it/u=1511364704,3337189105&fm=21&gp=0.jpg");
        sUrls.add("http://img3.imgtn.bdimg.com/it/u=2144096677,2391514122&fm=21&gp=0.jpg");
        sUrls.add("http://img0.imgtn.bdimg.com/it/u=556618733,1205300389&fm=21&gp=0.jpg");
        sUrls.add("http://img1.imgtn.bdimg.com/it/u=3272030875,860665188&fm=21&gp=0.jpg");
        sUrls.add("http://img1.imgtn.bdimg.com/it/u=2237658959,3726297486&fm=21&gp=0.jpg");

        sUrls.add("http://img4.duitang.com/uploads/item/201307/02/20130702113059_UEGL2.jpeg");
        sUrls.add("http://img0.imgtn.bdimg.com/it/u=985035006,79865976&fm=21&gp=0.jpg");
        sUrls.add("http://img5.imgtn.bdimg.com/it/u=1774291582,2563335167&fm=21&gp=0.jpg");
        sUrls.add("http://img5.imgtn.bdimg.com/it/u=1511364704,3337189105&fm=21&gp=0.jpg");
        sUrls.add("http://img3.imgtn.bdimg.com/it/u=2144096677,2391514122&fm=21&gp=0.jpg");
        sUrls.add("http://img0.imgtn.bdimg.com/it/u=556618733,1205300389&fm=21&gp=0.jpg");
        sUrls.add("http://img1.imgtn.bdimg.com/it/u=3272030875,860665188&fm=21&gp=0.jpg");
        sUrls.add("http://img1.imgtn.bdimg.com/it/u=2237658959,3726297486&fm=21&gp=0.jpg");
        sUrls.add("http://img1.imgtn.bdimg.com/it/u=3016675040,1510439865&fm=21&gp=0.jpg");
        sUrls.add("http://photocdn.sohu.com/20160307/mp62252655_1457334772519_2.png");

        sUrls.add("http://img0.imgtn.bdimg.com/it/u=556618733,1205300389&fm=21&gp=0.jpg");
        sUrls.add("http://img1.imgtn.bdimg.com/it/u=3272030875,860665188&fm=21&gp=0.jpg");
        sUrls.add("http://img1.imgtn.bdimg.com/it/u=2237658959,3726297486&fm=21&gp=0.jpg");
        sUrls.add("http://img1.imgtn.bdimg.com/it/u=3016675040,1510439865&fm=21&gp=0.jpg");
        sUrls.add("http://photocdn.sohu.com/20160307/mp62252655_1457334772519_2.png");
}
    private FrameLayout mContentContainer;//
    private RecyclerView mRecyclerView;//
    private LinearLayout.LayoutParams mLayoutParams;//
    private int mStatusBarHeight;//
    private int[] mPadding = new int[4];
    private int mSolidWidth = 0;
    private int mSolidHeight = 0;
    private Rect mRVBounds = new Rect();
    private GridLayoutManager mGridLayoutManager;//
    private List<ThumbViewInfo> mThumbViewInfoList = new ArrayList<>();
    private PreviewLayout mPreviewLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStatusBarHeight = getResources().getDimensionPixelSize(getResources().getIdentifier("status_bar_height", "dimen", "android"));

        mContentContainer = (FrameLayout) findViewById(android.R.id.content);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        mLayoutParams = new LinearLayout.LayoutParams(metrics.widthPixels / 3, metrics.widthPixels / 3);

        for (int i = 0; i < sUrls.size(); i++) {
            mThumbViewInfoList.add(new ThumbViewInfo(sUrls.get(i), i));
        }

        mGridLayoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(new BaseQuickAdapter<String>(R.layout.item_pic_thumb, sUrls) {
            @Override
            protected void convert(final BaseViewHolder holder, final String url) {
                holder.itemView.setLayoutParams(mLayoutParams);

                final ImageView thumbView = holder.getView(R.id.thumb_iv);
                Glide.with(MainActivity.this)
                        .load(url)
                        .into(thumbView);
            }
        });

        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                mPreviewLayout = new PreviewLayout(MainActivity.this);
                mPreviewLayout.setData(mThumbViewInfoList, position);
                mPreviewLayout.startScaleUpAnimation();
                mContentContainer.addView(mPreviewLayout);
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener(){


            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    assembleDataList();
                }
            }
        });
    }
















    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            mRecyclerView.getGlobalVisibleRect(mRVBounds);

            assembleDataList();
        }
    }

    private void assembleDataList() {
        computeBoundsForward(mGridLayoutManager.findFirstCompletelyVisibleItemPosition());

        computeBoundsBackward(mGridLayoutManager.findFirstCompletelyVisibleItemPosition());
    }

    /**
     * 从第一个完整可见item顺序遍历
     */
    private void computeBoundsForward(int firstCompletelyVisiblePos) {
        for (int i = firstCompletelyVisiblePos; i < mThumbViewInfoList.size(); i++) {
            View itemView = mGridLayoutManager.findViewByPosition(i);
            Rect bounds = new Rect();

            if (itemView != null) {
                ImageView thumbView = (ImageView) itemView.findViewById(R.id.thumb_iv);

                thumbView.getGlobalVisibleRect(bounds);

                if (mSolidWidth * mSolidHeight == 0) {
                    mPadding[0] = thumbView.getPaddingLeft();
                    mPadding[1] = thumbView.getPaddingTop();
                    mPadding[2] = thumbView.getPaddingRight();
                    mPadding[3] = thumbView.getPaddingBottom();
                    mSolidWidth = bounds.width();
                    mSolidHeight = bounds.height();
                }

                bounds.left = bounds.left + mPadding[0];
                bounds.top = bounds.top + mPadding[1];
                bounds.right = bounds.left + mSolidWidth - mPadding[2];
                bounds.bottom = bounds.top + mSolidHeight - mPadding[3];
            } else {
                bounds.left = i % 3 * mSolidWidth + mPadding[0];
                bounds.top = mRVBounds.bottom + mPadding[1];
                bounds.right = bounds.left + mSolidWidth - mPadding[2];
                bounds.bottom = bounds.top + mSolidHeight - mPadding[3];
            }
            bounds.offset(0, -mStatusBarHeight);

            mThumbViewInfoList.get(i).setBounds(bounds);
        }
    }

    /**
     * 从第一个完整可见item逆序遍历，如果初始位置为0，则不执行方法内循环
     */
    private void computeBoundsBackward(int firstCompletelyVisiblePos) {
        for (int i = firstCompletelyVisiblePos - 1; i >= 0; i--) {
            View itemView = mGridLayoutManager.findViewByPosition(i);
            Rect bounds = new Rect();

            if (itemView != null) {
                ImageView thumbView = (ImageView) itemView.findViewById(R.id.thumb_iv);

                thumbView.getGlobalVisibleRect(bounds);

                bounds.left = bounds.left + mPadding[0];
                bounds.bottom = bounds.bottom - mPadding[3];
                bounds.right = bounds.left + mSolidWidth - mPadding[2];
                bounds.top = bounds.bottom - mSolidHeight + mPadding[1];
            } else {
                bounds.left = i % 3 * mSolidWidth + mPadding[0];
                bounds.bottom = mRVBounds.top - mPadding[3];
                bounds.right = bounds.left + mSolidWidth - mPadding[2];
                bounds.top = bounds.bottom - mSolidHeight + mPadding[1];
            }
            bounds.offset(0, -mStatusBarHeight);

            mThumbViewInfoList.get(i).setBounds(bounds);
        }
    }

    @Override
    public void onBackPressed() {
        if (mContentContainer.getChildAt(mContentContainer.getChildCount() - 1) instanceof PreviewLayout) {
            mPreviewLayout.startScaleDownAnimation();
            return;
        }

        super.onBackPressed();
    }
}