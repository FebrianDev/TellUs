package ui.screens.post

import data.like.model.LikeRequest
import data.like.LikeSdk
import data.like.state.LikeState
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LikeViewModel : ViewModel() {

    private val sdk = LikeSdk()

    private val _likeState = MutableStateFlow<LikeState>(LikeState.Empty)
    val likeState: StateFlow<LikeState> get() = _likeState.asStateFlow()

//    fun getLikeById(likeRequest: LikeRequest ){
//        _likeState.value = LikeState.Loading
//        CoroutineScope(Dispatchers.IO).launch {
//            _likeState.value = sdk.getLikeById(likeRequest)
//        }
//    }

    fun insertLike(likeRequest: LikeRequest){
        _likeState.value = LikeState.Loading
        CoroutineScope(Dispatchers.IO).launch {
            _likeState.value = sdk.insertLike(likeRequest)
        }
    }
}