package com.nodecoyote.happybelly

import com.nodecoyote.happybelly.repo.HappyBellyRepository
import org.junit.Test

class RepositoryUnitTest: HappyBellyRepository {

    @Test
    fun getBusinessById_test(){
        assert(happyBellyRepository.getReview("DDRZIICbOHKSxTWsgSrVlQ") != null)
    }
}