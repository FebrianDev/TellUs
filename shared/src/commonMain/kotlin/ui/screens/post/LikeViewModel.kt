package ui.screens.post

import data.like.LikeSdk
import data.like.model.LikeRequest
import data.like.state.LikeState
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import utils.KeyValueStorage
import utils.KeyValueStorageImpl

class LikeViewModel : ViewModel() {

    private val sdk = LikeSdk()
    private val keyValueStorage: KeyValueStorage = KeyValueStorageImpl()

    private val _likeState = MutableStateFlow<Result<LikeState>>(Result.success(LikeState.Empty))
    val likeState: StateFlow<Result<LikeState>> get() = _likeState.asStateFlow()

    fun insertLike(likeRequest: LikeRequest) {
        _likeState.value = Result.success(LikeState.Loading)
        CoroutineScope(Dispatchers.IO).launch {
            keyValueStorage.observableApiToken?.collectLatest { apiToken ->
                _likeState.value = sdk.insertLike(likeRequest, apiToken)
            }
        }
    }
}