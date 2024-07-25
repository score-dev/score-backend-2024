package com.score.backend.repositories.user;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.score.backend.models.QUser;
import com.score.backend.models.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    QUser user = QUser.user;
    QUser friend = new QUser("friend");

    @Override
    public Page<User> findFriendsPage(long userId, Pageable pageable) {
        JPAQuery<User> where = queryFactory
                .select(friend)
                .from(user)
                .leftJoin(user.friends, friend)
                .where(user.id.eq(userId));
        List<User> users = where
                .orderBy(user.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        long total = where.stream().count();

        return new PageImpl<>(users, pageable, total);
    }
}
