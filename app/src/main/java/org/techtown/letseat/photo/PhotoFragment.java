package org.techtown.letseat.photo;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import org.techtown.letseat.MainActivity;
import org.techtown.letseat.R;
import org.techtown.letseat.ReviewSearch;

// 사진 탭에서 사진 클릭 시 나오는 플래그먼트
public class PhotoFragment extends Fragment {
    private ImageView photoView;
    private TextView title;
    private TextView review;
    private Button cancelButton;
    private Bitmap resdId;
    private String title_text;
    private String review_text;
    private RatingBar ratingBar2;
    private float resrate;
    PhotoList photoList;
    MainActivity mainActivity;
    ReviewSearch reviewSearch;
    public View onCreate(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.photo_fragment, container, false);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.photo_fragment, container, false);
        photoView = view.findViewById(R.id.photo_view);
        title = view.findViewById(R.id.photo_title);
        review = view.findViewById(R.id.photo_review);
        photoView.setImageBitmap(resdId);
        title.setText(title_text);
        review.setText(review_text);
        photoList = PhotoList.photoList;
        mainActivity = MainActivity.mainActivity;
        reviewSearch = ReviewSearch.reviewSearch;
        PhotoFragment photoFragment = this;
        cancelButton = view.findViewById(R.id.photo_cancel);
        ratingBar2 = view.findViewById(R.id.ratingBar2);
        ratingBar2.setRating(resrate);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            int resId = bundle.getInt("aP");
        }
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mainActivity.check == true){
                    mainActivity.check = false;
                }
                else if(photoList.check == true){
                    photoList.check = false;
                }
                else if(reviewSearch.check == true){
                    reviewSearch.check = false;
                }

                getActivity().getSupportFragmentManager().beginTransaction().remove(photoFragment).commit();
            }
        });
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    public void setresId(Bitmap resId){
        this.resdId = resId;
    }
    public void setTitle(String text){
        title_text = text;
    }
    public void setReview(String text){
        review_text = text;
    }
    public void setRate(float rate) {resrate = rate; }
}