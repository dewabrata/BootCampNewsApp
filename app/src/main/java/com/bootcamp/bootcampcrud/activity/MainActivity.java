package com.bootcamp.bootcampcrud.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.bootcamp.bootcampcrud.APIService.APIClient;
import com.bootcamp.bootcampcrud.APIService.APIInterfacesRest;
import com.bootcamp.bootcampcrud.ApplicationBootCamp;
import com.bootcamp.bootcampcrud.R;
import com.bootcamp.bootcampcrud.adapter.AdapterListNews;
import com.bootcamp.bootcampcrud.servicemodel.newsmodel.Bootcampnews;
import com.bootcamp.bootcampcrud.servicemodel.newsmodel.NewsModel;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.queriable.StringQuery;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;

import org.json.JSONObject;

import java.util.List;

import io.reactivex.annotations.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rc ;
    private AdapterListNews mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();
        com.nostra13.universalimageloader.core.ImageLoader.getInstance().init(config);


        rc  = (RecyclerView)findViewById(R.id.recyclerView);
        rc.setLayoutManager(new LinearLayoutManager(this));
        rc.setHasFixedSize(true);


        getNewsData();


    }

    private void getNewsData(){


        APIInterfacesRest apiInterface = APIClient.getClient().create(APIInterfacesRest.class);
        Call<NewsModel> market = apiInterface.getNews();


        market.enqueue(new Callback<NewsModel>() {
            @Override
            public void onResponse(Call<NewsModel> call, Response<NewsModel> response) {
                NewsModel news = response.body();


                if (response.body() != null) {
                    //  Toast.makeText(LoginActivity.this,userList.getToken().toString(),Toast.LENGTH_LONG).show();

                    if (news.getData().getBootcampnews().size()>0){

                        savedb(news.getData().getBootcampnews());
                        setAdapterList(news.getData().getBootcampnews());


                    }

                } else {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        Toast.makeText(MainActivity.this, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }


            }

            @Override
            public void onFailure(Call<NewsModel> call, Throwable t) {

                Toast.makeText(MainActivity.this, "Terjadi gangguan koneksi", Toast.LENGTH_LONG).show();
                call.cancel();
                getListOffline();
            }

        });
    }

    private void getListOffline (){

        setAdapterList(getDataNews());

    }


    private void setAdapterList(List<Bootcampnews> items){

        //set data and list adapter
        mAdapter = new AdapterListNews(this, items);
        rc.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new AdapterListNews.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Bootcampnews obj, int position) {

                Intent intent = new Intent(MainActivity.this,NewsDetail.class);
                Bundle b = new Bundle();
                b.putSerializable("data", obj);
                intent.putExtras(b);
                startActivity(intent);

            }
        });


    }

    public void savedb(List<Bootcampnews> items){

        FlowManager.getDatabase(ApplicationBootCamp.class)
                .beginTransactionAsync(new ProcessModelTransaction.Builder<>(
                        new ProcessModelTransaction.ProcessModel<Bootcampnews>() {
                            @Override
                            public void processModel(Bootcampnews orderItem, DatabaseWrapper wrapper) {

                                orderItem.save();


                            }

                        }).addAll(items).build())  // add elements (can also handle multiple)
                .error(new Transaction.Error() {
                    @Override
                    public void onError(Transaction transaction, Throwable error) {
                        Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                })
                .success(new Transaction.Success() {
                    @Override
                    public void onSuccess(Transaction transaction) {
                        Toast.makeText(getApplicationContext(),"Data Tersimpan",Toast.LENGTH_LONG).show();

                    }
                }).build().execute();


    }

    public List<Bootcampnews> getDataNews (){


        List<Bootcampnews> news = SQLite.select()
                .from(Bootcampnews.class)
            //    .where(User_Table.age.greaterThan(18))
                .queryList();

        return news;
    }

/*
    public void sqlQueryList(String orderno){

        String rawQuery = "SELECT distinct * FROM `Dataorder` where driver ='"+AppController.username+"' and shipmentNo ='"+orderno+"' group by shipmentNo;";
        StringQuery<Dataorder> stringQuery = new StringQuery<>(Dataorder.class, rawQuery);
        stringQuery
                .async()
                .queryListResultCallback(new QueryTransaction.QueryResultListCallback<Dataorder>() {
                                             @Override
                                             public void onListQueryResult(QueryTransaction transaction, @NonNull List<Dataorder> models) {

                                                 progress_bar.setVisibility(View.GONE);
                                                 fab.setAlpha(1f);
                                                 if (models.size()>0){

                                                     Intent intent = new Intent(getApplicationContext(),ShipToActivity.class);
                                                     intent.putExtra("shipmentno",models.get(0).getShipmentNo());
                                                     startActivityForResult(intent,999);

                                                 }else{
                                                     Toast.makeText(getApplicationContext(),"Document ID Not Found !",Toast.LENGTH_LONG).show();
                                                 }


                                             }
                                         }


                )
                .execute();
*/

    }




