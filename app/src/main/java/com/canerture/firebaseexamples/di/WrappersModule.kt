package com.canerture.firebaseexamples.di

import android.content.Context
import com.canerture.firebaseexamples.data.wrapper.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WrappersModule {

    @Provides
    @Singleton
    fun provideFirestoreOperationsWrapper(firestore: FirebaseFirestore) =
        FirestoreOperationsWrapper(firestore)

    @Provides
    @Singleton
    fun provideAuthOperationsWrapper(firebaseAuth: FirebaseAuth) =
        AuthOperationsWrapper(firebaseAuth)

    @Provides
    @Singleton
    fun provideStorageOperationsWrapper(firebaseStorage: FirebaseStorage) =
        StorageOperationsWrapper(firebaseStorage)

    @Provides
    @Singleton
    fun provideAdsOperationsWrapper(@ApplicationContext context: Context) =
        AdsOperationsWrapper(context)

    @Provides
    @Singleton
    fun provideDynamicLinksOperationsWrapper() = DynamicLinksOperationsWrapper()

    @Provides
    @Singleton
    fun provideRemoteConfigWrapper(firebaseRemoteConfig: FirebaseRemoteConfig) =
        RemoteConfigWrapper(firebaseRemoteConfig)
}