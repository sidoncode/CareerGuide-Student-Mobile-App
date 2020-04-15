package com.careerguide;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.careerguide.blog.DataMembers;
import com.careerguide.youtubeVideo.Videos;
import com.careerguide.youtubeVideo.Videos_ELEVEN;
import com.careerguide.youtubeVideo.Videos_GRADUATE;
import com.careerguide.youtubeVideo.Videos_NINE;
import com.careerguide.youtubeVideo.Videos_POSTGRA;
import com.careerguide.youtubeVideo.Videos_TEN;
import com.careerguide.youtubeVideo.Videos_TWELVE;
import com.careerguide.youtubeVideo.Videos_WORKING;
import com.careerguide.youtubeVideo.Videos_three;
import com.careerguide.youtubeVideo.Videos_two;

import java.util.ArrayList;
import java.util.List;

public class CGPlayListViewModel extends ViewModel {

    private MutableLiveData<ArrayList<Videos>> displaylistArray;
    private MutableLiveData<List<Videos_two>> displaylistArray_two;
    private MutableLiveData<List<Videos_three>> displaylistArray_three;
    private MutableLiveData<List<Videos_NINE>> displaylistArray_NINE;
    private MutableLiveData<List<Videos_TEN>> displaylistArray_TEN;
    private MutableLiveData<List<Videos_ELEVEN>> displaylistArray_ELEVEN;
    private MutableLiveData<List<Videos_TWELVE>> displaylistArray_TWELVE;
    private MutableLiveData<List<Videos_GRADUATE>> displaylistArray_GRADUATE;
    private MutableLiveData<List<Videos_POSTGRA>> displaylistArray_POSTGRA;
    private MutableLiveData<List<Videos_WORKING>> displaylistArray_WORKING;
    private MutableLiveData<List<DataMembers>> displaylistArray_Blog;

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

    }


    public MutableLiveData<ArrayList<Videos>> getDisplaylistArray() {
        return displaylistArray;
    }

    public void setDisplaylistArray(ArrayList<Videos> displaylistArray) {
        this.displaylistArray.setValue(displaylistArray);
    }

    public MutableLiveData<List<Videos_two>> getDisplaylistArray_two() {
        return displaylistArray_two;
    }

    public void setDisplaylistArray_two(List<Videos_two> displaylistArray_two) {
        this.displaylistArray_two.setValue(displaylistArray_two);
    }

    public MutableLiveData<List<Videos_three>> getDisplaylistArray_three() {
        return displaylistArray_three;
    }

    public void setDisplaylistArray_three(List<Videos_three> displaylistArray_three) {
        this.displaylistArray_three.setValue(displaylistArray_three);
    }

    public MutableLiveData<List<Videos_NINE>> getDisplaylistArray_NINE() {
        return displaylistArray_NINE;
    }

    public void setDisplaylistArray_NINE(List<Videos_NINE> displaylistArray_NINE) {
        this.displaylistArray_NINE.setValue(displaylistArray_NINE);
    }

    public MutableLiveData<List<Videos_TEN>> getDisplaylistArray_TEN() {
        return displaylistArray_TEN;
    }

    public void setDisplaylistArray_TEN(List<Videos_TEN> displaylistArray_TEN) {
        this.displaylistArray_TEN.setValue(displaylistArray_TEN);
    }

    public MutableLiveData<List<Videos_ELEVEN>> getDisplaylistArray_ELEVEN() {
        return displaylistArray_ELEVEN;
    }

    public void setDisplaylistArray_ELEVEN(List<Videos_ELEVEN> displaylistArray_ELEVEN) {
        this.displaylistArray_ELEVEN.setValue(displaylistArray_ELEVEN);
    }

    public MutableLiveData<List<Videos_TWELVE>> getDisplaylistArray_TWELVE() {
        return displaylistArray_TWELVE;
    }

    public void setDisplaylistArray_TWELVE(List<Videos_TWELVE> displaylistArray_TWELVE) {
        this.displaylistArray_TWELVE.setValue(displaylistArray_TWELVE);
    }

    public MutableLiveData<List<Videos_GRADUATE>> getDisplaylistArray_GRADUATE() {
        return displaylistArray_GRADUATE;
    }

    public void setDisplaylistArray_GRADUATE(List<Videos_GRADUATE> displaylistArray_GRADUATE) {
        this.displaylistArray_GRADUATE.setValue(displaylistArray_GRADUATE);
    }

    public MutableLiveData<List<Videos_POSTGRA>> getDisplaylistArray_POSTGRA() {
        return displaylistArray_POSTGRA;
    }

    public void setDisplaylistArray_POSTGRA(List<Videos_POSTGRA> displaylistArray_POSTGRA) {
        this.displaylistArray_POSTGRA.setValue(displaylistArray_POSTGRA);
    }

    public MutableLiveData<List<Videos_WORKING>> getDisplaylistArray_WORKING() {
        return displaylistArray_WORKING;
    }

    public void setDisplaylistArray_WORKING(List<Videos_WORKING> displaylistArray_WORKING) {
        this.displaylistArray_WORKING.setValue(displaylistArray_WORKING);
    }

    public MutableLiveData<List<DataMembers>> getDisplaylistArray_Blog() {
        return displaylistArray_Blog;
    }

    public void setDisplaylistArray_Blog(List<DataMembers> displaylistArray_Blog) {
        this.displaylistArray_Blog.setValue(displaylistArray_Blog);
    }
}