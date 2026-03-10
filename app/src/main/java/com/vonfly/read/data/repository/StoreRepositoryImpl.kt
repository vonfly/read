package com.vonfly.read.data.repository

import com.vonfly.read.domain.model.Banner
import com.vonfly.read.domain.model.Category
import com.vonfly.read.domain.model.StoreBook
import com.vonfly.read.domain.repository.StoreRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 书城仓库实现
 *
 * 本期使用硬编码 Mock 数据，后续替换为 Retrofit API。
 */
@Singleton
class StoreRepositoryImpl @Inject constructor() : StoreRepository {

    override fun getBanners(): Flow<List<Banner>> = flow {
        emit(
            listOf(
                Banner(
                    id = UUID.randomUUID().toString(),
                    title = "限时免费",
                    description = "精选好书限时免费阅读",
                    buttonText = "立即查看",
                    gradientStart = "#667eea",
                    gradientEnd = "#764ba2"
                ),
                Banner(
                    id = UUID.randomUUID().toString(),
                    title = "新人专享",
                    description = "首单立减 10 元",
                    buttonText = "立即领取",
                    gradientStart = "#f093fb",
                    gradientEnd = "#f5576c"
                ),
                Banner(
                    id = UUID.randomUUID().toString(),
                    title = "会员特惠",
                    description = "开通会员享 8 折优惠",
                    buttonText = "立即开通",
                    gradientStart = "#4facfe",
                    gradientEnd = "#00f2fe"
                )
            )
        )
    }

    override fun getCategories(): Flow<List<Category>> = flow {
        emit(
            listOf(
                Category(id = "recommend", name = "推荐"),
                Category(id = "novel", name = "小说"),
                Category(id = "literature", name = "文学"),
                Category(id = "history", name = "历史"),
                Category(id = "tech", name = "科技")
            )
        )
    }

    override fun getHotBooks(categoryId: String?): Flow<List<StoreBook>> = flow {
        val books = when (categoryId) {
            "novel" -> getNovelBooks()
            "literature" -> getLiteratureBooks()
            "history" -> getHistoryBooks()
            "tech" -> getTechBooks()
            else -> getRecommendBooks() // null 或 "recommend" 返回推荐书籍
        }
        emit(books)
    }

    private fun getRecommendBooks(): List<StoreBook> = listOf(
        StoreBook(
            id = UUID.randomUUID().toString(),
            rank = 1,
            title = "三体",
            author = "刘慈欣",
            rating = 9.4f,
            readCount = "892万人读过",
            coverGradientStart = "#667eea",
            coverGradientEnd = "#764ba2"
        ),
        StoreBook(
            id = UUID.randomUUID().toString(),
            rank = 2,
            title = "活着",
            author = "余华",
            rating = 9.3f,
            readCount = "756万人读过",
            coverGradientStart = "#f093fb",
            coverGradientEnd = "#f5576c"
        ),
        StoreBook(
            id = UUID.randomUUID().toString(),
            rank = 3,
            title = "百年孤独",
            author = "加西亚·马尔克斯",
            rating = 9.2f,
            readCount = "634万人读过",
            coverGradientStart = "#4facfe",
            coverGradientEnd = "#00f2fe"
        ),
        StoreBook(
            id = UUID.randomUUID().toString(),
            rank = 4,
            title = "人类简史",
            author = "尤瓦尔·赫拉利",
            rating = 9.1f,
            readCount = "521万人读过",
            coverGradientStart = "#43e97b",
            coverGradientEnd = "#38f9d7"
        ),
        StoreBook(
            id = UUID.randomUUID().toString(),
            rank = 5,
            title = "围城",
            author = "钱钟书",
            rating = 9.0f,
            readCount = "489万人读过",
            coverGradientStart = "#fa709a",
            coverGradientEnd = "#fee140"
        )
    )

    private fun getNovelBooks(): List<StoreBook> = listOf(
        StoreBook(
            id = UUID.randomUUID().toString(),
            rank = 1,
            title = "红楼梦",
            author = "曹雪芹",
            rating = 9.6f,
            readCount = "1024万人读过",
            coverGradientStart = "#ff6b6b",
            coverGradientEnd = "#feca57"
        ),
        StoreBook(
            id = UUID.randomUUID().toString(),
            rank = 2,
            title = "西游记",
            author = "吴承恩",
            rating = 9.5f,
            readCount = "987万人读过",
            coverGradientStart = "#5f27cd",
            coverGradientEnd = "#a29bfe"
        ),
        StoreBook(
            id = UUID.randomUUID().toString(),
            rank = 3,
            title = "三国演义",
            author = "罗贯中",
            rating = 9.4f,
            readCount = "876万人读过",
            coverGradientStart = "#00d2d3",
            coverGradientEnd = "#54a0ff"
        ),
        StoreBook(
            id = UUID.randomUUID().toString(),
            rank = 4,
            title = "水浒传",
            author = "施耐庵",
            rating = 9.3f,
            readCount = "765万人读过",
            coverGradientStart = "#ff9f43",
            coverGradientEnd = "#feca57"
        ),
        StoreBook(
            id = UUID.randomUUID().toString(),
            rank = 5,
            title = "平凡的世界",
            author = "路遥",
            rating = 9.2f,
            readCount = "654万人读过",
            coverGradientStart = "#10ac84",
            coverGradientEnd = "#1dd1a1"
        )
    )

    private fun getLiteratureBooks(): List<StoreBook> = listOf(
        StoreBook(
            id = UUID.randomUUID().toString(),
            rank = 1,
            title = "追风筝的人",
            author = "卡勒德·胡赛尼",
            rating = 9.1f,
            readCount = "543万人读过",
            coverGradientStart = "#ee5a24",
            coverGradientEnd = "#f79f1f"
        ),
        StoreBook(
            id = UUID.randomUUID().toString(),
            rank = 2,
            title = "挪威的森林",
            author = "村上春树",
            rating = 9.0f,
            readCount = "432万人读过",
            coverGradientStart = "#6ab04c",
            coverGradientEnd = "#badc58"
        ),
        StoreBook(
            id = UUID.randomUUID().toString(),
            rank = 3,
            title = "白夜行",
            author = "东野圭吾",
            rating = 8.9f,
            readCount = "321万人读过",
            coverGradientStart = "#eb4d4b",
            coverGradientEnd = "#ff7979"
        ),
        StoreBook(
            id = UUID.randomUUID().toString(),
            rank = 4,
            title = "解忧杂货店",
            author = "东野圭吾",
            rating = 8.8f,
            readCount = "234万人读过",
            coverGradientStart = "#686de0",
            coverGradientEnd = "#a29bfe"
        ),
        StoreBook(
            id = UUID.randomUUID().toString(),
            rank = 5,
            title = "小王子",
            author = "安托万·德·圣埃克苏佩里",
            rating = 9.2f,
            readCount = "876万人读过",
            coverGradientStart = "#f9ca24",
            coverGradientEnd = "#f0932b"
        )
    )

    private fun getHistoryBooks(): List<StoreBook> = listOf(
        StoreBook(
            id = UUID.randomUUID().toString(),
            rank = 1,
            title = "明朝那些事儿",
            author = "当年明月",
            rating = 9.3f,
            readCount = "765万人读过",
            coverGradientStart = "#e15b64",
            coverGradientEnd = "#f8b500"
        ),
        StoreBook(
            id = UUID.randomUUID().toString(),
            rank = 2,
            title = "万历十五年",
            author = "黄仁宇",
            rating = 9.1f,
            readCount = "456万人读过",
            coverGradientStart = "#30336b",
            coverGradientEnd = "#596275"
        ),
        StoreBook(
            id = UUID.randomUUID().toString(),
            rank = 3,
            title = "全球通史",
            author = "斯塔夫里阿诺斯",
            rating = 9.0f,
            readCount = "345万人读过",
            coverGradientStart = "#22a6b3",
            coverGradientEnd = "#7ed6df"
        ),
        StoreBook(
            id = UUID.randomUUID().toString(),
            rank = 4,
            title = "史记",
            author = "司马迁",
            rating = 9.5f,
            readCount = "567万人读过",
            coverGradientStart = "#6c5ce7",
            coverGradientEnd = "#a29bfe"
        ),
        StoreBook(
            id = UUID.randomUUID().toString(),
            rank = 5,
            title = "罗马人的故事",
            author = "盐野七生",
            rating = 8.9f,
            readCount = "234万人读过",
            coverGradientStart = "#fd79a8",
            coverGradientEnd = "#fdcb6e"
        )
    )

    private fun getTechBooks(): List<StoreBook> = listOf(
        StoreBook(
            id = UUID.randomUUID().toString(),
            rank = 1,
            title = "深入理解计算机系统",
            author = "Randal E.Bryant",
            rating = 9.7f,
            readCount = "123万人读过",
            coverGradientStart = "#2d3436",
            coverGradientEnd = "#636e72"
        ),
        StoreBook(
            id = UUID.randomUUID().toString(),
            rank = 2,
            title = "代码整洁之道",
            author = "Robert C.Martin",
            rating = 9.2f,
            readCount = "98万人读过",
            coverGradientStart = "#00b894",
            coverGradientEnd = "#55efc4"
        ),
        StoreBook(
            id = UUID.randomUUID().toString(),
            rank = 3,
            title = "设计模式",
            author = "GoF",
            rating = 9.4f,
            readCount = "87万人读过",
            coverGradientStart = "#0984e3",
            coverGradientEnd = "#74b9ff"
        ),
        StoreBook(
            id = UUID.randomUUID().toString(),
            rank = 4,
            title = "算法导论",
            author = "Thomas H.Cormen",
            rating = 9.3f,
            readCount = "76万人读过",
            coverGradientStart = "#d63031",
            coverGradientEnd = "#ff7675"
        ),
        StoreBook(
            id = UUID.randomUUID().toString(),
            rank = 5,
            title = "人工智能：现代方法",
            author = "Stuart Russell",
            rating = 9.1f,
            readCount = "65万人读过",
            coverGradientStart = "#6c5ce7",
            coverGradientEnd = "#a29bfe"
        )
    )
}
