package com.xh.xiaoshuo.mode

import com.qy.reader.common.entity.book.SearchBook
import com.xh.xiaoshuo.bean.BookBean
import com.xh.xiaoshuo.database.BookBeanDao
import com.xh.xiaoshuo.util.DBManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class BookDB {

    suspend fun query(mBook: SearchBook, mSource: SearchBook.SL): BookBean {
        return withContext(Dispatchers.IO) {
            val bookDao = DBManager.getInstance().session.bookBeanDao
            val bookList = bookDao.queryBuilder().where(
                    BookBeanDao.Properties.Name.eq(mBook.title),
                    BookBeanDao.Properties.Link.eq(mSource.link))
                    .orderDesc(BookBeanDao.Properties.OpenTime)
                    .list()
            if (bookList.size == 0) {
                val book = BookBean()
                book.name = mBook.title
                book.imageUrl = mBook.cover
                book.auther = mBook.author
                book.desc = mBook.desc
                book.link = mSource.link
                book.sourceId = mSource.source.id
                book.sourceName = mSource.source.name
                book.sourceUrl = mSource.source.searchURL
                book.readNum = 1
                book.readPage = 1
                book.state = 0
                book.openTime = System.currentTimeMillis()
                bookDao.insert(book)
                book
            } else if (bookList.size == 1) {
                bookList[0]
            } else {
                for (i in 1 until bookList.size) {
                    bookDao.delete(bookList[i])
                }
                bookList[0]
            }
        }
    }

    suspend fun update(bookBean: BookBean, isUpdateTime: Boolean = true) {
        withContext(Dispatchers.IO) {
            if (isUpdateTime) {
                bookBean?.openTime = System.currentTimeMillis()
            }
            val bookDao = DBManager.getInstance().session.bookBeanDao
            bookDao.update(bookBean)
        }

    }

    suspend fun queryBookshelfList(): MutableList<BookBean> {
        return withContext(Dispatchers.IO) {
            val list = mutableListOf<BookBean>()
            val bookDao = DBManager.getInstance().session.bookBeanDao
            val list1 = bookDao.queryBuilder()
                    .orderDesc(BookBeanDao.Properties.OpenTime)
                    .list()
            if (list1.size > 0 && list1[0].state == 0) {
                list.add(list1[0])
            }
            if (list1.size > 20) {
                for (i in 21 until list1.size) {
                    bookDao.delete(list1[i])
                }
            }

            val list2 = bookDao.queryBuilder()
                    .where(BookBeanDao.Properties.State.eq(1))
                    .orderDesc(BookBeanDao.Properties.OpenTime).list()
            list.addAll(list2)
            list
        }

    }

    suspend fun queryHistoryList(): MutableList<BookBean> {
        return withContext(Dispatchers.IO) {
            val bookDao = DBManager.getInstance().session.bookBeanDao
            bookDao.queryBuilder()
                    .orderDesc(BookBeanDao.Properties.OpenTime)
                    .list()
        }
    }


}