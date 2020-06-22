package cn.thundergaba.todaysfootline;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.http.I;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PersonFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PersonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PersonFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String TAG="woshishabi";

    public PersonFragment() {
        // Required empty public constructor
    }
    private PersonFragment.OnFragmentInteractionListener mListener;





    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PersonFragment newInstance(String param1, String param2) {
        PersonFragment fragment = new PersonFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //BmobUser.logOut();
        View view;
        if (BmobUser.isLogin()) {
            view=inflater.inflate(R.layout.fragment_person, container, false);
            ImageView pictureview = view.findViewById(R.id.PersonalPhoto);
            TextView personname = view.findViewById(R.id.person_name);
            Button personedit = view.findViewById(R.id.person_edit);
            Button praiseContent=view.findViewById(R.id.praise_content);
            Button commentContent=view.findViewById(R.id.comment);
            Button personlogout=view.findViewById(R.id.logout);
            Integer picturesource;
            HashMap<Integer, Integer> picturemap = new HashMap<Integer, Integer>();
            picturemap.put(R.id.imageView2, R.drawable.picture1);
            picturemap.put(R.id.imageView3, R.drawable.picture2);

            User user = BmobUser.getCurrentUser(User.class);



            String pictureidd = user.getAvatar();
            Log.d(TAG,user.getAvatar());
            Integer pictureid=Integer.parseInt(pictureidd);
            //personname.setText(pictureidd);
            picturesource = picturemap.get(pictureid);
            pictureview.setImageDrawable(getResources().getDrawable(picturesource));
            personname.setText(user.getUsername());
            praiseContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(),ShowPraise.class));
                }
            });
//
            commentContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(),ShowComment.class));
                }
            });

            personedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(),ChangeInfo.class));
                }
            });

            personlogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BmobUser.logOut();
                    startActivity(new Intent(getActivity(),personmoduledev.class));
                }
            });

        }
        else{
            view=inflater.inflate(R.layout.fragment_person1,container,false);
            Snackbar.make(view,"您还没有登录，请点击选择",Snackbar.LENGTH_SHORT);
            Button Tologin=view.findViewById(R.id.Tologin);
            Button Tosign=view.findViewById(R.id.Tosign);
            Tologin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(),FirstChoose.class));
                }
            });
            Tosign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(getActivity(),Register.class));
                }
            });
        }

        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof PersonFragment.OnFragmentInteractionListener) {
            mListener = (PersonFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }



    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


}