package com.knyamagoudar.nytimessearch.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.knyamagoudar.nytimessearch.R;
import com.knyamagoudar.nytimessearch.models.Filter;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FilterFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FilterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilterFragment extends DialogFragment implements DatePickerFragment.OnFragmentInteractionListener, DatePickerFragment.DatePickerDialogListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    private ImageButton ibBeginDate;
    private ImageButton ibEndDate;
    private TextView tvBeginDate;
    private TextView tvEndDate;
    private Spinner spSortOrder;
    private Button btnSave;
    private CheckBox cbArts;
    private CheckBox cbFashioStyle;
    private CheckBox cbSports;



    String[] sortOrderArray;
    private OnFragmentInteractionListener mListener;

    public FilterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FilterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FilterFragment newInstance(String param1, String param2) {
        FilterFragment fragment = new FilterFragment();
        Bundle args = new Bundle();
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
        return inflater.inflate(R.layout.fragment_filter, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ibBeginDate = (ImageButton) view.findViewById(R.id.ibBeginDate);
        tvBeginDate = (TextView) view.findViewById((R.id.tvBeginDate));

        ibEndDate = (ImageButton) view.findViewById(R.id.ibEndDate);
        tvEndDate = (TextView) view.findViewById((R.id.tvEndDate));

        cbArts = (CheckBox) view.findViewById(R.id.checkbox_arts);
        cbFashioStyle = (CheckBox) view.findViewById(R.id.checkbox_fashion);
        cbSports = (CheckBox) view.findViewById(R.id.checkbox_sports);

        ibBeginDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDateButttonClick("begin_date");
            }
        });

        ibEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onDateButttonClick("end_date");
            }
        });

        spSortOrder = (Spinner) view.findViewById(R.id.spSortOrder);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.sortorder_array, android.R.layout.simple_spinner_item);
        sortOrderArray = getResources().getStringArray(R.array.sortorder_array);
        spSortOrder.setAdapter(adapter);

        btnSave = (Button) view.findViewById(R.id.btnSaveFilterData);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDateIntoFilter();
            }
        });
    }

    private void saveDateIntoFilter(){

        Filter.beginDate =(tvBeginDate.getText().toString());
        Filter.endDate = (tvEndDate.getText().toString());
        Filter.sortOrder = (sortOrderArray[spSortOrder.getSelectedItemPosition()]);

        ArrayList newsDeskValues = new ArrayList<String>();
        if(cbArts.isChecked()){
            newsDeskValues.add("Arts");
        }
        if(cbFashioStyle.isChecked()){
            newsDeskValues.add("Fashion & Style");
        }
        if(cbSports.isChecked()){
            newsDeskValues.add("Sports");
        }
        Filter.newsDeskValues = (newsDeskValues);
        dismiss();
    }

    private int getIndex(String[] strArray,String str){
        for (int i=0;i<strArray.length;i++){
            if(Objects.equals(new String(strArray[i]),new String(str))){
                return i;
            }
        }
        return 0;
    }

    public void onDateButttonClick(String btnName){
        FragmentManager fm = getFragmentManager();
        DatePickerFragment datePickerDialogFragment = DatePickerFragment.newInstance(tvBeginDate.getText().toString(),btnName);
        // SETS the target fragment for use later when sending results
        datePickerDialogFragment.setTargetFragment(FilterFragment.this, 300);
        datePickerDialogFragment.show(fm, "fragment_edit_name");

    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onFinishDatePickDialog(String inputText,String btnType) {
        if(btnType == "begin_date"){
            tvBeginDate.setText(inputText);
        }
        if(btnType == "end_date"){
            tvEndDate.setText(inputText);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
