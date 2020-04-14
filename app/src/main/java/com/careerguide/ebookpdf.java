package com.careerguide;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ebookpdf extends Fragment {

        ebookpdf activity = this;

    public ebookpdf() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_ebook, container, false);
        view.findViewById(R.id.newage1).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), WebViewActivity.class);

            intent.putExtra("filename", "self-career-counselling-ebook.pdf");
            intent.putExtra("url","https://www.careerguide.com/career/wp-content/uploads/2019/09/self-career-counselling-ebook.pdf");
            startActivity(intent);
        });
        view.findViewById(R.id.age_careerr).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), WebViewActivity.class);

            intent.putExtra("filename", "self-career-counselling-ebook.pdf");
            intent.putExtra("url","https://www.careerguide.com/career/wp-content/uploads/2019/09/self-career-counselling-ebook.pdf");

            startActivity(intent);
        });

        view.findViewById(R.id.newage).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra("filename", "new-age-careers-careers-that-didnt-exist-20-yr-ago.pdf");
            intent.putExtra("url","https://www.careerguide.com/career/wp-content/uploads/2019/05/new-age-careers-careers-that-didnt-exist-20-yr-ago.pdf");

            startActivity(intent);
        });
        view.findViewById(R.id.age_career).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra("filename", "new-age-careers-careers-that-didnt-exist-20-yr-ago.pdf");
            intent.putExtra("url","https://www.careerguide.com/career/wp-content/uploads/2019/05/new-age-careers-careers-that-didnt-exist-20-yr-ago.pdf");
            startActivity(intent);
        });
        return view;
    }

}
//    String message = "Choose today Enjoy tomorrow - Make right career choices";
//    File fileBrochure = new File( Environment.getExternalStorageDirectory().getPath() + "/" + pdfsname );
//                if (!fileBrochure.exists()) {
//                        CopyAssetsbrochure();
//                        }
//
//                        /** PDF reader code */
//                        File file = new File( Environment.getExternalStorageDirectory().getPath() + "/" + pdfsname );
//
//                        Intent intent = new Intent( Intent.ACTION_SEND );
//                        intent.putExtra( Intent.EXTRA_STREAM, Uri.fromFile( file ) );
//                        intent.setType( "application/pdf" );
//                        intent.putExtra( Intent.EXTRA_TEXT, message );
//                        intent.addFlags( Intent.FLAG_GRANT_READ_URI_PERMISSION );
//                        intent.setPackage( "com.whatsapp" );
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        try {
//                        startActivity( intent );
//                        } catch (ActivityNotFoundException e) {
//                        Toast.makeText( ShareActivity.this, "NO Pdf Viewer", Toast.LENGTH_SHORT ).show();
//                        }
//                        }
//
////method to write the PDFs file to sd card
//private void CopyAssetsbrochure() {
//        AssetManager assetManager = getAssets();
//        String[] files = null;
//        try {
//        files = assetManager.list( "" );
//        } catch (IOException e) {
//        Log.e( "tag", e.getMessage() );
//        }
//        for (int i = 0; i < files.length; i++) {
//        String fStr = files[i];
//        if (fStr.equalsIgnoreCase( pdfsname)) {
//        InputStream in = null;
//        OutputStream out = null;
//        try {
//        in = assetManager.open( files[i] );
//        out = new FileOutputStream ( Environment.getExternalStorageDirectory() + "/" + files[i] );
//        copyFile( in, out );
//        in.close();
//        out.flush();
//        out.close();
//
//        break;
//        } catch (Exception e) {
//        Log.e( "tag", e.getMessage() );
//        }
//        }
//        }
//        }
//        } );
//        }
//
//        private void copyFile(InputStream in, OutputStream out) throws IOException {
//        byte[] buffer = new byte[1024];
//        int read;
//        while((read = in.read(buffer)) != -1){
//        out.write(buffer, 0, read);
//        }
//        }