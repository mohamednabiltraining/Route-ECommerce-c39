package com.route.data.api

import android.util.Log
import com.route.data.api.webServices.AddressWebServices
import com.route.data.api.webServices.AuthenticationWebServices
import com.route.data.api.webServices.CategoryWebServices
import com.route.data.api.webServices.ProductWebServices
import com.route.data.api.webServices.SubcategoryWebServices
import com.route.data.api.webServices.WishlistWebServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor =
            HttpLoggingInterceptor {
                Log.e("retrofit", "$it")
            }
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return loggingInterceptor
    }

    @Provides
    fun provideOkHttp(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    fun provideGson(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }

    @Provides
    fun provideRetrofit(
        okhttp: OkHttpClient,
        gson: GsonConverterFactory,
    ): Retrofit {
        return Retrofit.Builder()
            .client(okhttp)
            .addConverterFactory(gson)
            .baseUrl("https://ecommerce.routemisr.com")
            .build()
    }

    @Provides
    fun provideAddressWebServices(retrofit: Retrofit): AddressWebServices {
        return retrofit.create(AddressWebServices::class.java)
    }

    @Provides
    fun provideAuthenticationWebServices(retrofit: Retrofit): AuthenticationWebServices {
        return retrofit.create(AuthenticationWebServices::class.java)
    }

    @Provides
    fun provideCategoryWebServices(retrofit: Retrofit): CategoryWebServices {
        return retrofit.create(CategoryWebServices::class.java)
    }

    @Provides
    fun provideProductWebServices(retrofit: Retrofit): ProductWebServices {
        return retrofit.create(ProductWebServices::class.java)
    }

    @Provides
    fun provideSubcategoryWebServices(retrofit: Retrofit): SubcategoryWebServices {
        return retrofit.create(SubcategoryWebServices::class.java)
    }

    @Provides
    fun provideWishlistWebServices(retrofit: Retrofit): WishlistWebServices {
        return retrofit.create(WishlistWebServices::class.java)
    }
}
