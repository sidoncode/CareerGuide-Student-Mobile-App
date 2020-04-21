package com.careerguide;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.careerguide.blog.DataMembers;
import com.careerguide.blog.model.CategoryDetails;
import com.careerguide.youtubeVideo.CommonEducationModel;
import com.careerguide.youtubeVideo.Videos;

import java.util.ArrayList;
import java.util.List;

public class CGPlayListViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Videos>> displaylistArray;
    private MutableLiveData<List<Videos>> displaylistArray_two;
    private MutableLiveData<List<Videos>> displaylistArray_three;
    private MutableLiveData<List<CommonEducationModel>> displaylistArray_NINE;
    private MutableLiveData<List<CommonEducationModel>> displaylistArray_TEN;
    private MutableLiveData<List<CommonEducationModel>> displaylistArray_ELEVEN;
    private MutableLiveData<List<CommonEducationModel>> displaylistArray_TWELVE;
    private MutableLiveData<List<CommonEducationModel>> displaylistArray_GRADUATE;
    private MutableLiveData<List<CommonEducationModel>> displaylistArray_POSTGRA;
    private MutableLiveData<List<CommonEducationModel>> displaylistArray_WORKING;
    private MutableLiveData<List<DataMembers>> displaylistArray_Blog;
    private MutableLiveData<List<CategoryDetails>> displaylistArray_categoryDetails;

    public CGPlayListViewModel() {
        displaylistArray = new MutableLiveData<>();
        displaylistArray_two = new MutableLiveData<>();
        displaylistArray_three = new MutableLiveData<>();
        displaylistArray_NINE = new MutableLiveData<>();
        displaylistArray_TEN = new MutableLiveData<>();
        displaylistArray_ELEVEN = new MutableLiveData<>();
        displaylistArray_TWELVE = new MutableLiveData<>();
        displaylistArray_GRADUATE = new MutableLiveData<>();
        displaylistArray_POSTGRA = new MutableLiveData<>();
        displaylistArray_WORKING = new MutableLiveData<>();
        displaylistArray_Blog = new MutableLiveData<>();
        displaylistArray_categoryDetails = new MutableLiveData<>();

    }


    public MutableLiveData<ArrayList<Videos>> getDisplaylistArray() {
        return displaylistArray;
    }

    public void setDisplaylistArray(ArrayList<Videos> displaylistArray) {
        this.displaylistArray.setValue(displaylistArray);
    }

    public MutableLiveData<List<Videos>> getDisplaylistArray_two() {
        return displaylistArray_two;
    }

    public void setDisplaylistArray_two(List<Videos> displaylistArray_two) {
        this.displaylistArray_two.setValue(displaylistArray_two);
    }

    public MutableLiveData<List<Videos>> getDisplaylistArray_three() {
        return displaylistArray_three;
    }

    public void setDisplaylistArray_three(List<Videos> displaylistArray_three) {
        this.displaylistArray_three.setValue(displaylistArray_three);
    }

    public MutableLiveData<List<CommonEducationModel>> getDisplaylistArray_NINE() {
        return displaylistArray_NINE;
    }

    public void setDisplaylistArray_NINE(List<CommonEducationModel> displaylistArray_NINE) {
        this.displaylistArray_NINE.setValue(displaylistArray_NINE);
    }

    public MutableLiveData<List<CommonEducationModel>> getDisplaylistArray_TEN() {
        return displaylistArray_TEN;
    }

    public void setDisplaylistArray_TEN(List<CommonEducationModel> displaylistArray_TEN) {
        this.displaylistArray_TEN.setValue(displaylistArray_TEN);
    }

    public MutableLiveData<List<CommonEducationModel>> getDisplaylistArray_ELEVEN() {
        return displaylistArray_ELEVEN;
    }

    public void setDisplaylistArray_ELEVEN(List<CommonEducationModel> displaylistArray_ELEVEN) {
        this.displaylistArray_ELEVEN.setValue(displaylistArray_ELEVEN);
    }

    public MutableLiveData<List<CommonEducationModel>> getDisplaylistArray_TWELVE() {
        return displaylistArray_TWELVE;
    }

    public void setDisplaylistArray_TWELVE(List<CommonEducationModel> displaylistArray_TWELVE) {
        this.displaylistArray_TWELVE.setValue(displaylistArray_TWELVE);
    }

    public MutableLiveData<List<CommonEducationModel>> getDisplaylistArray_GRADUATE() {
        return displaylistArray_GRADUATE;
    }

    public void setDisplaylistArray_GRADUATE(List<CommonEducationModel> displaylistArray_GRADUATE) {
        this.displaylistArray_GRADUATE.setValue(displaylistArray_GRADUATE);
    }

    public MutableLiveData<List<CommonEducationModel>> getDisplaylistArray_POSTGRA() {
        return displaylistArray_POSTGRA;
    }

    public void setDisplaylistArray_POSTGRA(List<CommonEducationModel> displaylistArray_POSTGRA) {
        this.displaylistArray_POSTGRA.setValue(displaylistArray_POSTGRA);
    }

    public MutableLiveData<List<CommonEducationModel>> getDisplaylistArray_WORKING() {
        return displaylistArray_WORKING;
    }

    public void setDisplaylistArray_WORKING(List<CommonEducationModel> displaylistArray_WORKING) {
        this.displaylistArray_WORKING.setValue(displaylistArray_WORKING);
    }

    public MutableLiveData<List<DataMembers>> getDisplaylistArray_Blog() {
        return displaylistArray_Blog;
    }

    public void setDisplaylistArray_Blog(List<DataMembers> displaylistArray_Blog) {
        this.displaylistArray_Blog.setValue(displaylistArray_Blog);
    }

    public MutableLiveData<List<CategoryDetails>> getDisplaylistArray_categoryDetails() {
        return displaylistArray_categoryDetails;
    }

    public void setDisplaylistArray_categoryDetails(List<CategoryDetails> displaylistArray_categoryDetails) {
        this.displaylistArray_categoryDetails.setValue(displaylistArray_categoryDetails);
    }
}