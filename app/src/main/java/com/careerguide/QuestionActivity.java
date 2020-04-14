package com.careerguide;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Random;

public class QuestionActivity extends AppCompatActivity {

    private int currentQuestion = -1;
    private boolean passageShown = false;
    private int totalQue;
    private ArrayList<QuestionAndOptions> questionAndOptionses;
    private Activity activity = this;
    private View nextButton;
    private CircleDisplay circleDisplay;
    Bundle bundle = new Bundle();
    private boolean[] pauseShown = new boolean[Utility.sectionSet.size()];

    //private int[] animations = {R.anim.alpha_maximize,R.anim.alpha_minimize,R.anim.rotate_left_maximize,R.anim.rotate_right_maximize};
    private Techniques[] techniques = {Techniques.BounceIn, Techniques.BounceInDown,Techniques.BounceInLeft,Techniques.BounceInRight,Techniques.BounceInUp
            ,Techniques.FadeIn,Techniques.FadeInDown,Techniques.FadeInLeft,Techniques.FadeInUp,Techniques.FadeInRight
            ,Techniques.RotateIn,Techniques.RotateInDownLeft,Techniques.RotateInDownRight,Techniques.RotateInUpLeft,Techniques.RotateInUpRight
            ,Techniques.FlipInX,Techniques.FlipInY,Techniques.FlipInX,Techniques.FlipInY
            ,Techniques.ZoomIn,Techniques.ZoomInDown,Techniques.ZoomInLeft,Techniques.ZoomInRight,Techniques.ZoomInUp
            ,Techniques.RotateIn,Techniques.RotateInDownLeft,Techniques.RotateInDownRight,Techniques.RotateInUpLeft,Techniques.RotateInUpRight
            ,Techniques.SlideInDown,Techniques.SlideInUp,Techniques.SlideInLeft,Techniques.SlideInRight
            ,Techniques.FlipInX,Techniques.FlipInY
            ,Techniques.ZoomIn,Techniques.ZoomInDown,Techniques.ZoomInLeft,Techniques.ZoomInRight,Techniques.ZoomInUp};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
//        getSupportActionBar().setElevation(0);


       // Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
      //  setSupportActionBar(myToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("");

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            activity.getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }*/
        Log.e("position","On Create");
        if(savedInstanceState != null)
        {
            Log.e("position","On Create not null");
            currentQuestion = savedInstanceState.getInt("currentQuestion");
            passageShown = savedInstanceState.getBoolean("passageShown");
            pauseShown = savedInstanceState.getBooleanArray("pauseShown");
            Utility.questionAndOptionses  = (ArrayList<QuestionAndOptions>) savedInstanceState.getSerializable("questionAndOptionses");
            Utility.paragraphs = (ArrayList<String>) savedInstanceState.getSerializable("paragraphs");
            Utility.sectionSet = (ArrayList<String>) savedInstanceState.getSerializable("sectionSet");
        }
        questionAndOptionses = Utility.questionAndOptionses;
        totalQue = questionAndOptionses.size();
        circleDisplay = findViewById(R.id.progress);
        circleDisplay.setColor(getResources().getColor(R.color.app_yellow));
        circleDisplay.setTextSize(14);
        circleDisplay.setValueWidthPercent(22);
        circleDisplay.setTouchEnabled(false);
        circleDisplay.setFormatDigits(0);
        circleDisplay.setDimAlpha(0);
        circleDisplay.setAnimDuration(0);

        CircleDisplay backgroundCircle = findViewById(R.id.progressBackground);
        backgroundCircle.showValue(100,100,false);
        backgroundCircle.setColor(Color.parseColor("#d3d3d3"));
        backgroundCircle.setDrawText(false);
        backgroundCircle.setValueWidthPercent(22);

        nextButton = findViewById(R.id.next);
        findViewById(R.id.previous).setOnClickListener(v -> {
            currentQuestion--;
            displayQues();
            prevNextState();

        });

        nextButton.setOnClickListener(v -> {
            currentQuestion++;
            if(questionAndOptionses.get(currentQuestion).getQuestion().getType().equals("passage_based") && !passageShown)
            {
                passageShown = true;
                Intent intent = new Intent(activity,PassageActivity.class);
                startActivity(intent);
            }
            if(currentQuestion > 0)
            {
                if(questionAndOptionses.get(currentQuestion-1).getAnswerKey().equals(""))
                {
                    //Toast.makeText(activity, "Please answer this question first.",Toast.LENGTH_SHORT).show();
                    currentQuestion--;
                    return;
                }
                else
                {
                    displayQues();
                }
            }
            else {
                displayQues();
            }
            prevNextState();

        });

        for (QuestionAndOptions questionAndOptions :Utility.questionAndOptionses)
        {
            String answerKey = questionAndOptions.getAnswerKey();
            if (!answerKey.equals("")) {
                currentQuestion++;
            } else {
                break;
            }
        }

        if(nextState())
        {
            nextButton.performClick();
        }
        else
        {
            Intent intent = new Intent(activity,PauseActivity.class);
            intent.putExtra("heading","Congratulations!");
            intent.putExtra("description","You've completed the ideal career test.\nYou are about to find out your ideal career.\nDownload the report now.");
            intent.putExtra("imageResource",R.drawable.section_eight);
            intent.putExtra("color","#9323c7");
            intent.putExtra("finished",true);
            intent.putExtra("auth",getIntent().getStringExtra("auth"));
            startActivity(intent);
            finish();
        }
        //findViewById(R.id.next).performClick();

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.e("position","On Restore");
        if(savedInstanceState != null)
        {
            Log.e("position","On Restore not null");
            currentQuestion = savedInstanceState.getInt("currentQuestion");
            passageShown = savedInstanceState.getBoolean("passageShown");
            pauseShown = savedInstanceState.getBooleanArray("pauseShown");
            Utility.questionAndOptionses  = (ArrayList<QuestionAndOptions>) savedInstanceState.getSerializable("questionAndOptionses");
            Utility.paragraphs = (ArrayList<String>) savedInstanceState.getSerializable("paragraphs");
            Utility.sectionSet = (ArrayList<String>) savedInstanceState.getSerializable("sectionSet");
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        Log.e("position","On Saved Instance State");
        outState.putInt("currentQuestion",currentQuestion);
        outState.putBoolean("passageShown",passageShown);
        outState.putSerializable("questionAndOptionses",Utility.questionAndOptionses);
        outState.putSerializable("paragraphs",Utility.paragraphs);
        outState.putSerializable("sectionSet",Utility.sectionSet);
        outState.putSerializable("pauseShown",pauseShown);
        super.onSaveInstanceState(outState);
    }

    private void prevNextState() {
        nextState();
        prevState();
    }

    private boolean nextState()
    {
        if(currentQuestion < totalQue-1)
        {
            //findViewById(R.id.next).setVisibility(View.VISIBLE);
            return true;
        }
        else
        {
            //findViewById(R.id.next).setVisibility(View.INVISIBLE);
            return false;
        }
    }

    private boolean prevState()
    {
        if(currentQuestion > 0)
        {
            findViewById(R.id.previous).setVisibility(View.VISIBLE);
            return true;
        }
        else
        {
            findViewById(R.id.previous).setVisibility(View.INVISIBLE);
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        if(prevState())
        {
            findViewById(R.id.previous).performClick();
        }
        else {
            super.onBackPressed();
        }
    }

    private void displayQues()
    {
        final QuestionAndOptions questionAndOptions = questionAndOptionses.get(currentQuestion);
        Question question = questionAndOptions.getQuestion();
        final int sectionNumber = Utility.sectionSet.indexOf(question.getSection()) + 1;

        if(currentQuestion +1 == questionAndOptionses.size())
        {
            circleDisplay.setColor(Color.parseColor("#4caf50"));
        }
        Random r = new Random();
        int index = r.nextInt(techniques.length);
//        Animation animation = AnimationUtils.loadAnimation(activity, animations[index]);
        //findViewById(R.id.parentRL).startAnimation(animation);
        //Log.e("random",index + "");
        final int optionAnimDuration = 1150;
        YoYo.with(Techniques.FadeIn/*techniques[index]*/)
                .duration(950)
                .repeat(0)
                /*.playOn(findViewById(R.id.parentRL));*/
                .playOn(findViewById(R.id.questionLL));
        YoYo.with(Techniques.FadeIn/*techniques[index]*/)
                .duration(optionAnimDuration)
                .repeat(0)
                /*.playOn(findViewById(R.id.parentRL));*/
                .playOn(findViewById(R.id.answers));

        TextView queInfo = findViewById(R.id.queInfo);
        TextView questionTextView = findViewById(R.id.que);
        NetworkImageView networkImageView = findViewById(R.id.queImage);
        final ArrayList<Option> options = questionAndOptions.getOptions();
        final TextView optionTextViews[] = new TextView[4];
        NetworkImageView optionNetworkImageViews[] = new NetworkImageView[4];
        final FrameLayout optionFramelayout[] = new FrameLayout[4];
        circleDisplay.showValue(((currentQuestion+1)*100)/questionAndOptionses.size(),100,true);

        if(questionAndOptions.getQuestion().getType().equals("passage_based"))
        {
            findViewById(R.id.button).setVisibility(View.VISIBLE);
        }
        else
        {
            findViewById(R.id.button).setVisibility(View.GONE);
        }
        findViewById(R.id.button).setOnClickListener(v -> {
            passageShown = true;
            Intent intent = new Intent(activity,PassageActivity.class);
            startActivity(intent);
        });
        optionTextViews[0] = findViewById(R.id.option1TextView);
        optionTextViews[1] = findViewById(R.id.option2TextView);
        optionTextViews[2] = findViewById(R.id.option3TextView);
        optionTextViews[3] = findViewById(R.id.option4TextView);

        optionNetworkImageViews[0] = findViewById(R.id.option1Image);
        optionNetworkImageViews[1] = findViewById(R.id.option2Image);
        optionNetworkImageViews[2] = findViewById(R.id.option3Image);
        optionNetworkImageViews[3] = findViewById(R.id.option4Image);

        optionFramelayout[0] = findViewById(R.id.option1LL);
        optionFramelayout[1] = findViewById(R.id.option2LL);
        optionFramelayout[2] = findViewById(R.id.option3LL);
        optionFramelayout[3] = findViewById(R.id.option4LL);


        LinearLayout answerRow2 = findViewById(R.id.answerRow2);


        if(options.size() > 2)
        {
            answerRow2.setVisibility(View.VISIBLE);
        }
        else
        {
            answerRow2.setVisibility(View.GONE);
        }
        if(options.size() > 3)
        {
            optionFramelayout[3].setVisibility(View.VISIBLE);
        }
        else {
            optionFramelayout[3].setVisibility(View.GONE);
        }


        queInfo.setText("Section " + sectionNumber + ": Question " + question.getSrNo());
        if( question instanceof QuestionImageBased)
        {
            networkImageView.setVisibility(View.VISIBLE);
            String questionText = ( (QuestionImageBased) question).getTitle();
            Log.e("questiontext", questionText);
            Document document = Jsoup.parse(questionText);
            Elements element = document.select("img[src]");
            String imgSrc = element.attr("src");
            Log.e("image url", imgSrc);
            document.select("img[src]").remove();
            networkImageView.setImageUrl(imgSrc, VolleySingleton.getInstance(activity).getImageLoader());
           // String rachitsrc = "http://www.univariety.meracareerguide.com/assessment/ideal/Assessment-Images/s-q1.jpg";
            networkImageView.setImageUrl(imgSrc, VolleySingleton.getInstance(activity).getImageLoader());
            questionTextView.setText(Html.fromHtml(document.toString())  /*+ "\n\nOriginal\n" + questionText*/);
            Log.e("#rachit","rachiturl" +imgSrc);
        }
        else if( question instanceof QuestionTextBased)
        {

            String questionText = ( (QuestionTextBased) question).getTitle();
            /* Document document = Jsoup.parse(questionText);
            Elements element = document.select("div[class=phyho_int_ques]");*/
            //Log.e("Question", questionText);
            questionTextView.setText(Html.fromHtml(questionText)  /*+ "\n\nOriginal\n" + questionText*/);
            networkImageView.setVisibility(View.GONE);
        }
        String answerKey = questionAndOptions.getAnswerKey();
        if(answerKey.equals(""))
        {
            nextButton.setVisibility(View.INVISIBLE);
        }
        else
        {

            nextButton.setVisibility(View.VISIBLE);
        }
        for(int i = 0; i<options.size(); i++)
        {
            optionFramelayout[i].setBackground(getResources().getDrawable(R.drawable.answer_background));
            if(answerKey.equals(options.get(i).getKey()))
            {
                optionFramelayout[i].setBackground(getResources().getDrawable(R.drawable.answer_selected_background));
            }
            final Option option = options.get(i);
            if(option instanceof OptionTextBased)
            {
                optionNetworkImageViews[i].setVisibility(View.GONE);
                optionTextViews[i].setVisibility(View.VISIBLE);
                optionTextViews[i].setText( ((OptionTextBased)option).getValue() );
                optionTextViews[i].setTextColor(Color.BLACK);
                if(answerKey.equals(options.get(i).getKey()))
                {
                    optionTextViews[i].setTextColor(getResources().getColor(R.color.white));
                }
            }
            else if(option instanceof OptionImageBased)
            {
                optionTextViews[i].setVisibility(View.GONE);
                optionNetworkImageViews[i].setVisibility(View.VISIBLE);
                String optionText = ((OptionImageBased)option).getValue();
                Document document = Jsoup.parse(optionText);
                Elements element = document.select("img[src]");
                String imgSrc = element.attr("src");
                Log.e("##imageurl", imgSrc);
                optionNetworkImageViews[i].setImageUrl(imgSrc, VolleySingleton.getInstance(activity).getImageLoader());
            }

            final int finalI = i;
            new Handler().postDelayed(() -> optionFramelayout[finalI].setOnClickListener(v -> {
                optionFramelayout[finalI].setOnClickListener(null);
                int delay = 200;

                Animation fadeIn = AnimationUtils.loadAnimation(activity, R.anim.fade_in);
                optionFramelayout[finalI].setBackgroundResource(R.drawable.answer_selected_background);
                optionFramelayout[finalI].startAnimation(fadeIn);

                new Handler().postDelayed(() -> {
                    optionTextViews[finalI].setTextColor(Color.WHITE);
                    for(int j = 0; j< options.size(); j++)
                    {
                        if(j != finalI)
                        {
                            optionFramelayout[j].setBackground(getResources().getDrawable(R.drawable.answer_background));
                            optionTextViews[j].setTextColor(Color.BLACK);
                        }
                    }
                },0);
                final Handler handler = new Handler();
                handler.postDelayed(() -> {
                    int nextSectionNumber = sectionNumber;
                    if(currentQuestion < totalQue -1 )
                    {
                        nextSectionNumber = Utility.sectionSet.indexOf(questionAndOptionses.get(currentQuestion + 1).getQuestion().getSection()) + 1;
                    }
                    Log.e("sectionNumber",sectionNumber + "");
                    Log.e("previousSectionNumber" , nextSectionNumber + "");
                    Log.e("pauseShown" , pauseShown[sectionNumber-1] + "");
                    if (sectionNumber != nextSectionNumber && !pauseShown[sectionNumber-1])
                    {
                        pauseShown[sectionNumber-1] = true;
                        Intent intent = new Intent(activity,PauseActivity.class);
                        intent.putExtra("auth",getIntent().getStringExtra("auth"));
                        switch (nextSectionNumber)
                        {
                            case 2:
                                intent.putExtra("heading","Well done!");
                                intent.putExtra("description","You have successfully completed your first milestone. Let us do some more exciting exercises.In this exercise, imagine yourself in a work-life situation, where you need to do certain tasks to earn your daily bread and butter.");
                                intent.putExtra("imageResource",R.drawable.section_one);
                                intent.putExtra("color","#4e59ad");
                                break;
                            case 3:
                                intent.putExtra("heading","Great going!");
                                intent.putExtra("description","Your journey towards discovering your ideal career has reached a new milestone.The following exercise is very interesting. You will be solving visual puzzles. Here, you need to carefully observe the diagram in the question. Then select the option which you think completes the sequence.");
                                intent.putExtra("imageResource",R.drawable.section_two);
                                intent.putExtra("color","#d82a4b");
                                break;
                            case 4:
                                intent.putExtra("heading","You have been answering so well!");
                                intent.putExtra("description","This exercise involves solving visual puzzles. You need to find out two codes that are similar to each other and choose the correct option.");
                                intent.putExtra("imageResource",R.drawable.section_seven);
                                intent.putExtra("color","#269065");
                                break;
                            case 5:
                                intent.putExtra("heading","Nicely done!");
                                intent.putExtra("description","In this exercise, you need to imagine yourself in some logical scenarios and mark you answers accordingly.");
                                intent.putExtra("imageResource",R.drawable.section_three);
                                intent.putExtra("color","#42cc52");
                                break;
                            case 6:
                                intent.putExtra("heading","Awesome! You are doing great.");
                                intent.putExtra("description","Through this exercise, you will figure out priorities you have regarding your best suited career. Interestingly, there is no right or wrong answer. Select the answer that first comes to your mind.");
                                intent.putExtra("imageResource",R.drawable.section_four);
                                intent.putExtra("color","#289abf");
                                break;
                            case 7:
                                intent.putExtra("heading","And, it's almost over!");
                                intent.putExtra("description","Answer these last set of questions. This exercise is all about your personality. Select the option that strikes you as the right one. Relax! Don’t think too hard.");
                                intent.putExtra("imageResource",R.drawable.section_six);
                                intent.putExtra("color","#269065");
                                break;
                            default:
                                intent.putExtra("heading","You have done an amazing job!");
                                intent.putExtra("description","In this exercise, you need to imagine yourself in some logical scenarios and mark you answers accordingly.");
                                intent.putExtra("imageResource",R.drawable.section_five);
                                intent.putExtra("color","#b38f31");
                        }
                        startActivityForResult(intent,2378);
                    }
                    else if(nextState())
                    {
                        nextButton.performClick();
                    }
                    else
                    {
                        Intent intent = new Intent(activity,PauseActivity.class);
                        intent.putExtra("auth",getIntent().getStringExtra("auth"));
                        intent.putExtra("heading","Congratulations!");
                        intent.putExtra("description","You've completed the ideal career test.\nYou are about to find out your ideal career.\nDownload the report now.");
                        intent.putExtra("imageResource",R.drawable.section_eight);
                        intent.putExtra("color","#9323c7");
                        intent.putExtra("finished",true);
                        startActivity(intent);
                        finish();
                    }
                }, delay + 300);
                questionAndOptions.setAnswerKey(option.getKey());
                Utility.saveAnswer(activity);
            }),optionAnimDuration);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 2378)
        {
            nextButton.performClick();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_question,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_exit)
        {
            finish();
            return true;
        }
        if (item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Utility.handleOnlineStatus(this, "idle");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Utility.handleOnlineStatus(null,"");
    }
}
