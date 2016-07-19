package com.test.myapplication2.app;//package com.sharif.PomoDo;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.widget.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by mina on 7/9/16.
// */
//public class ToDoListTestFragment extends Fragment implements View.OnClickListener  {
//
////    ListView listView;
//
//    OnSwipeTouchListener onSwipeTouchListener;
//    List<String> list = new ArrayList<String>();
//    ImageView delete;
//    CustomAdapter adapter;
//    ListView listView;
//    int position = 0;
//
//    @Override
//    public void onClick(View view) {
//        if (view.getId() == R.id.btnAdd){
//            list.remove(position);
//            delete.setVisibility(View.INVISIBLE);
//            adapter.notifyDataSetChanged();
//        }
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.test, container, false);
//
//        onSwipeTouchListener = new OnSwipeTouchListener();
//
//        Button btn = (Button) view.findViewById(R.id.btnAdd);
//        listView = (ListView) view.findViewById(R.id.listview);
//
//        Log.d("lbtn", btn == null ? "null":"not null");
//        /** Defining the ArrayAdapter to set items to ListView */
//        adapter = new CustomAdapter(getContext(), list);
//
//        /** Defining a click event listener for the button "Add" */
//        View.OnClickListener listener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                EditText edit = (EditText) view.findViewById(R.id.txtItem);
//                list.add(edit.getText().toString());
//                edit.setText("");
//                adapter.notifyDataSetChanged();
//            }
//        };
//        listView.setOnTouchListener(new OnSwipeTouchListener(getContext(), listView) {
//
//            @Override
//            public void onSwipeRight(int pos) {
//
//                Toast.makeText(getActivity(), "right", Toast.LENGTH_LONG).show();
//                showDeleteButton(pos);
//            }
//
//            @Override
//            public void onSwipeLeft() {
//                Toast.makeText(getActivity(), "left", Toast.LENGTH_LONG).show();
//            }
//        });
//        btn.setOnClickListener(listener);
//        /** Setting the adapter to the ListView */
//        listView.setAdapter(adapter);
//
////        listView = (ListView) view.findViewById(R.id.listView2);
////
////        listView.setOnTouchListener(new OnSwipeTouchListener(getActivity().getBaseContext(), listView) {
////
////            @Override
////            public void onSwipeRight(int pos) {
////
////                Toast.makeText(getActivity(), "right", Toast.LENGTH_LONG).show();
//////                showDeleteButton(pos);
////            }
////
////            @Override
////            public void onSwipeLeft() {
////                Toast.makeText(getActivity(), "left", Toast.LENGTH_LONG).show();
////            }
////        });
//////        btn.setOnClickListener(listener);
//////        /** Setting the adapter to the ListView */
//////        listView.setAdapter(adapter);
//
//        return view;
//    }
//
//    private boolean showDeleteButton(int pos) {
//        position = pos;
//        View child = listView.getChildAt(pos - listView.getFirstVisiblePosition());
//        if (child != null) {
//
//            delete = (ImageView) child.findViewById(R.id.delete);
//            if (delete != null) {
//                if (delete.getVisibility() == View.INVISIBLE) {
//                    Animation animation =
//                            AnimationUtils.loadAnimation(getContext(),
//                                    R.anim.slide_out_left);
//                    delete.startAnimation(animation);
//                    delete.setVisibility(View.VISIBLE);
//                } else {
//                    Animation animation =
//                            AnimationUtils.loadAnimation(getContext(),
//                                    R.anim.slide_in_right);
//                    delete.startAnimation(animation);
//                    delete.setVisibility(View.INVISIBLE);
//                }
//            }
//            return true;
//        }
//        return false;
//    }
//
//
////
////    @Override
////    public boolean dispatchTouchEvent(MotionEvent ev) {
////
////        return super.dispatchTouchEvent(ev);
////    }
//
//}
