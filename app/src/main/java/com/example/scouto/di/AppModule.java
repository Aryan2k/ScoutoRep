package com.example.scouto.di;

import android.content.Context;

import androidx.room.Room;

import com.example.scouto.db.ScoutoDatabase;
import com.example.scouto.network.api.CarApi;
import com.example.scouto.repository.HomeRepository;
import com.example.scouto.repository.HomeRepositoryImpl;
import com.example.scouto.utils.Constants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Singleton
    @Provides
    static ScoutoDatabase provideScoutoDatabase(@ApplicationContext Context context) {
        return Room.databaseBuilder(
                context,
                ScoutoDatabase.class,
                Constants.DATABASE_NAME
        ).build();
    }

    @Provides
    static String provideBaseUrl() {
        return Constants.BASE_URL;
    }

    @Singleton
    @Provides
    static OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder()
                .build();
    }

    @Singleton
    @Provides
    static Retrofit provideRetrofit(OkHttpClient okHttpClient, String baseUrl) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .build();
    }

    @Provides
    @Singleton
    static CarApi provideApiService(Retrofit retrofit) {
        return retrofit.create(CarApi.class);
    }

    @Singleton
    @Provides
    static HomeRepository provideHomeRepository(HomeRepositoryImpl homeRepositoryImpl) {
        return homeRepositoryImpl;
    }

}
