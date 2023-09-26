package com.xw.samlpe.scaletransitionanim;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * PreviewLayout
 * <p/>
 * Created by jiangzhengyan on 2017-03-22.
 */
public class PreviewLayout extends FrameLayout implements ViewPager.OnPageChangeListener {

    public static final long ANIM_DURATION = 300;

    private View mBackgroundView;
    private HackyViewPager mViewPager;
    private ImageView mScalableImageView;
    private TextView mTvindicator;

    private List<ThumbViewInfo> mThumbViewInfoList = new ArrayList<>();
    private int mIndex;
    private Rect mStartBounds = new Rect();
    private Rect mFinalBounds = new Rect();
    private boolean isAnimFinished = true;

    public PreviewLayout(Context context) {
        this(context, null);
    }

    public PreviewLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PreviewLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context).inflate(R.layout.layout_preview, this, true);
        mBackgroundView = findViewById(R.id.background_view);
        mViewPager = (HackyViewPager) findViewById(R.id.view_pager);
        mScalableImageView = (ImageView) findViewById(R.id.scalable_image_view);
        mTvindicator = (TextView) findViewById(R.id.tv_indicator);

        mScalableImageView.setPivotX(0f);
        mScalableImageView.setPivotY(0f);
        mScalableImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
    }

    public void setData(List<ThumbViewInfo> list, int index) {
        if (list == null || list.isEmpty() || index < 0) {
            return;
        }

        mTvindicator.setText(index+1+"/"+list.size());
        this.mThumbViewInfoList = list;
        this.mIndex = index;
        mStartBounds = mThumbViewInfoList.get(mIndex).getBounds();

        post(new Runnable() {
            @Override
            public void run() {
                mViewPager.setAdapter(new ImagePagerAdapter());
                mViewPager.setCurrentItem(mIndex);
                mViewPager.addOnPageChangeListener(PreviewLayout.this);

                mScalableImageView.setX(mStartBounds.left);
                mScalableImageView.setY(mStartBounds.top);
                Glide.with(getContext()).load(mThumbViewInfoList.get(mIndex).getUrl()).into(mScalableImageView);
            }
        });
    }

    public void startScaleUpAnimation() {
        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                Point globalOffset = new Point();
                getGlobalVisibleRect(mFinalBounds, globalOffset);
                mFinalBounds.offset(-globalOffset.x, -globalOffset.y);

                LayoutParams lp = new LayoutParams(
                        mStartBounds.width(),
                        mStartBounds.width() * mFinalBounds.height() / mFinalBounds.width()
                );
                mScalableImageView.setLayoutParams(lp);

                startAnim();

                if (Build.VERSION.SDK_INT >= 16) {
                    getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onPageSelected(int position) {
        mIndex = position;
        mStartBounds = mThumbViewInfoList.get(mIndex).getBounds();
        if (mStartBounds == null) {
            return;
        }
        mTvindicator.setText(position+1+"/"+mThumbViewInfoList.size());

        computeStartScale();
    }

    private void startAnim() {
        if (!isAnimFinished) return;

        float startScale = computeStartScale();

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(mBackgroundView, View.ALPHA, 0f, 1f),
                ObjectAnimator.ofFloat(mScalableImageView, View.X, mStartBounds.left, mFinalBounds.left),
                ObjectAnimator.ofFloat(mScalableImageView, View.Y, mStartBounds.top, mFinalBounds.top),
                ObjectAnimator.ofFloat(mScalableImageView, View.SCALE_X, 1f / startScale),
                ObjectAnimator.ofFloat(mScalableImageView, View.SCALE_Y, 1f / startScale));
        animatorSet.setDuration(ANIM_DURATION);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isAnimFinished = false;
                mViewPager.setAlpha(0f);
                mScalableImageView.setVisibility(VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimFinished = true;
                mViewPager.setAlpha(1f);
                mScalableImageView.setVisibility(View.INVISIBLE);
            }
        });
        animatorSet.start();
    }

    private float computeStartScale() {
        float startScale;
        if ((float) mFinalBounds.width() / mFinalBounds.height()
                > (float) mStartBounds.width() / mStartBounds.height()) {
            // Extend start bounds horizontally （以竖直方向为参考缩放）
            startScale = (float) mStartBounds.height() / mFinalBounds.height();
            float startWidth = startScale * mFinalBounds.width();
            float deltaWidth = (startWidth - mStartBounds.width()) / 2;
            mStartBounds.left -= deltaWidth;
            mStartBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically （以水平方向为参考缩放）
            startScale = (float) mStartBounds.width() / mFinalBounds.width();
            float startHeight = startScale * mFinalBounds.height();
            float deltaHeight = (startHeight - mStartBounds.height()) / 2;
            mStartBounds.top -= deltaHeight;
            mStartBounds.bottom += deltaHeight;
        }

        return startScale;
    }

    public void startScaleDownAnimation() {
        if (!isAnimFinished) return;

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(mScalableImageView, View.X, mStartBounds.left),
                ObjectAnimator.ofFloat(mScalableImageView, View.Y, mStartBounds.top),
                ObjectAnimator.ofFloat(mScalableImageView, View.SCALE_X, 1f),
                ObjectAnimator.ofFloat(mScalableImageView, View.SCALE_Y, 1f));
        animatorSet.setDuration(ANIM_DURATION);
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isAnimFinished = false;
                Glide.with(getContext()).load(mThumbViewInfoList.get(mIndex).getUrl()).into(mScalableImageView);
                mScalableImageView.setVisibility(View.VISIBLE);
                mViewPager.setAlpha(0f);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                isAnimFinished = true;
                FrameLayout contentContainer = (FrameLayout) ((Activity) getContext()).findViewById(android.R.id.content);
                contentContainer.removeView(PreviewLayout.this);
            }
        });
        animatorSet.start();
    }

    private class ImagePagerAdapter extends PagerAdapter {
        public Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        Toast.makeText(getContext(), msg.obj.toString(), Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            }
        };
        @Override
        public int getCount() {
            return mThumbViewInfoList != null ? mThumbViewInfoList.size() : 0;
        }

        @Override
        public View instantiateItem(final ViewGroup container, final int position) {
            PhotoView photoView = new PhotoView(getContext());
            photoView.setOnLongClickListener(new OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //保存图片
                    savePic(mThumbViewInfoList.get(position).getUrl());
                    return false;
                }
            });
            Glide.with(getContext()).load(mThumbViewInfoList.get(position).getUrl()).into(photoView);
            photoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
                @Override
                public void onViewTap(View view, float x, float y) {
                    startScaleDownAnimation();
                }
            });

            container.addView(photoView);

            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
        /**
         * 保存图片到相册
         *
         * @param
         */
        private void savePic(final String imageUrl) {

            final Dialog dialog = new Dialog(getContext(), R.style.Theme_Light_Dialog_Theme_Light_Dialog_Cus);
            View view = View.inflate(getContext(), R.layout.dialog_save_pic, null);
            view.findViewById(R.id.tv_sure).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //确定
                    final Bitmap[] bitmap_toast = {null};

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            bitmap_toast[0] = getNetBitmap(imageUrl);
                             saveBitmap(getContext(), bitmap_toast[0]);
                            Message message = Message.obtain();
                            message.what = 1;
                            message.obj = "图片保存成功！";
                            handler.sendMessage(message);
                        }
                    }).start();
                    dialog.dismiss();
                }
            });
            view.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //取消
                    dialog.dismiss();
                }
            });
            dialog.setContentView(view);

//        dialog.setCancelable(false);

            dialog.show();
        }

        public void saveBitmap(Context context, Bitmap bitmap) {
            String sdCardDir = Environment.getExternalStorageDirectory() + "/DCIM/";
            File appDir = new File(sdCardDir, "ToastImage");
            if (!appDir.exists()) {
                appDir.mkdir();
            }
            String fileName = "ToastImage" + System.currentTimeMillis() + ".jpg";
            File f = new File(appDir, fileName);
            try {
                FileOutputStream fos = new FileOutputStream(f);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 通知图库更新  
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(f);
            intent.setData(uri);
            context.sendBroadcast(intent);
        }
        //2017-2-13 根据网络图片url转换为bitmap
        private Bitmap getNetBitmap(String url) {
            URL myFileUrl = null;
            Bitmap bitmap = null;
            try {
                myFileUrl = new URL(url);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                bitmap = android.graphics.BitmapFactory.decodeStream(is);
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }
    }

}
