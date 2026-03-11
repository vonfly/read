package com.vonfly.read.domain.usecase

import com.vonfly.read.domain.model.PageContent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import javax.inject.Inject

/**
 * 获取书籍内容 UseCase
 *
 * 本期使用 Mock 数据，模拟《三体》的书籍内容。
 * 后续接入真实数据源时，替换为从 Room/网络/EPUB 解析获取数据。
 */
class GetBookContentUseCase @Inject constructor() {

    /**
     * 获取书籍的分页内容
     *
     * @param bookId 书籍 ID
     * @return 分页内容列表
     */
    suspend operator fun invoke(bookId: String): Result<ImmutableList<PageContent>> = try {
        // Mock 数据：《三体》内容
        val pages = createMockPages(bookId)
        Result.success(pages)
    } catch (e: Exception) {
        if (e is kotlinx.coroutines.CancellationException) throw e
        Result.failure(e)
    }

    private fun createMockPages(bookId: String): ImmutableList<PageContent> {
        return persistentListOf(
            // 第1章
            PageContent(
                pageIndex = 0,
                chapterTitle = "第1章 科学边界",
                paragraphs = listOf(
                    "叶文洁坐在清华大学的图书馆里，面前摊开着一份发黄的文件。这是她父亲留下的遗物，一份关于红岸基地的绝密档案。",
                    "窗外的阳光透过玻璃洒进来，照在那些褪色的字迹上。叶文洁轻轻抚摸着纸张，仿佛能感受到父亲当年的气息。",
                    "\"红岸工程\"——这个名字在她父亲生前从未被提起过。直到父亲去世后，她才在整理遗物时发现了这个被隐藏的秘密。",
                    "档案中记载着一个令人难以置信的计划：向宇宙深处发射信号，寻找外星文明。这个计划始于1968年，持续了整整八年。",
                    "叶文洁的父亲是这个计划的核心成员之一。作为无线电物理学家，他负责设计发射天线和编码系统。"
                )
            ),
            PageContent(
                pageIndex = 1,
                paragraphs = listOf(
                    "她继续往下读，档案中提到了一次异常的接收记录。1971年9月23日，红岸基地接收到了一串规律的信号，但无法破译。",
                    "这个发现让叶文洁的心跳加速。难道父亲真的收到了来自外星的回复？",
                    "档案的最后一页是一封未寄出的信，是父亲写给她的。信中只有一句话：\"小洁，如果有一天你看到这些，请记住，人类的命运已经被改变了。\"",
                    "叶文洁合上档案，望向窗外。阳光依旧明媚，但她感到一阵深深的寒意。",
                    "她决定，要找到真相。"
                )
            ),
            PageContent(
                pageIndex = 2,
                paragraphs = listOf(
                    "第二天，叶文洁来到了中国科学院，找到了父亲的老同事——现已退休的物理学家李志强。",
                    "李志强住在科学院家属院的一栋老旧居民楼里。当叶文洁表明身份后，老人的脸上闪过一丝复杂的表情。",
                    "\"你是叶哲泰的女儿？\"老人问，声音有些颤抖。",
                    "\"是的，李叔叔。我在父亲的遗物中发现了红岸基地的档案，想向您了解一些情况。\"",
                    "李志强沉默了很长时间，然后叹了口气：\"进来吧，有些事情，是时候让你知道了。\""
                )
            ),
            // 第2章
            PageContent(
                pageIndex = 3,
                chapterTitle = "第2章 三体",
                paragraphs = listOf(
                    "李志强家里很简朴，到处都是书籍和杂志。他给叶文洁倒了一杯茶，然后坐到沙发上，点燃了一支烟。",
                    "\"红岸工程，\"老人缓缓说道，\"那是一段不该被遗忘，却又必须被遗忘的历史。\"",
                    "\"为什么必须被遗忘？\"",
                    "\"因为，\"李志强深吸一口气，\"我们收到了回复。\""
                )
            ),
            PageContent(
                pageIndex = 4,
                paragraphs = listOf(
                    "叶文洁的心跳几乎停止了。\"收到了...什么回复？\"",
                    "\"一封来自四光年外的信。\"李志强的声音变得低沉，\"那封信改变了人类的一切。\"",
                    "他站起身，从一个上锁的抽屉里取出一个布满灰尘的文件夹。\"这是我当年私自保留的副本。官方的所有记录都被销毁了，但我不能让真相消失。\"",
                    "叶文洁接过文件夹，手在微微颤抖。她打开第一页，看到的是一串数字和符号。",
                    "\"这是...什么？\"",
                    "\"这是三体世界的信息。\"李志强说，\"一个有着三颗太阳的世界，一个在混沌中挣扎的文明。\""
                )
            ),
            PageContent(
                pageIndex = 5,
                paragraphs = listOf(
                    "接下来的几个小时里，李志强向叶文洁讲述了红岸工程的全貌。",
                    "1968年，中国在大兴安岭深处建立了红岸基地，目的是探测外星文明。这个计划一直持续到1976年。",
                    "1971年9月23日，基地接收到了一串异常信号。经过三年多的破译，科学家们终于确认：这是一封来自半人马座α星系的信。",
                    "发信者自称来自\"三体世界\"——一个围绕三颗恒星运行的行星。那里的文明已经存在了数亿年，但因为三颗太阳的无规律运动，一直无法稳定发展。",
                    "\"他们的太阳系统太混乱了，\"李志强解释道，\"有时候三颗太阳同时升起，有时候一颗都没有。文明反复毁灭，又反复重建。\""
                )
            ),
            PageContent(
                pageIndex = 6,
                paragraphs = listOf(
                    "\"所以，他们想找一个新家园？\"叶文洁问。",
                    "李志强点点头：\"是的，他们发现了地球。在他们眼中，这是一个天堂——只有一颗太阳，气候稳定，适合生存。\"",
                    "\"那我们...回复了吗？\"",
                    "老人的表情变得痛苦：\"有一个人回复了。一个叫雷志成的工程师，他在不知情的情况下发出了回复信号。\"",
                    "\"内容是什么？\"",
                    "\"很简单：'这里是地球，我们欢迎你们。'\"",
                    "叶文洁感到一阵眩晕。这个简单的信息，可能意味着人类文明的终结。"
                )
            )
        )
    }
}
