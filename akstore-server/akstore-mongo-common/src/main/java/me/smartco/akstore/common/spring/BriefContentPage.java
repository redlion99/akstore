package me.smartco.akstore.common.spring;

import com.fasterxml.jackson.annotation.JsonView;
import me.smartco.akstore.common.model.BriefResult;
import me.smartco.akstore.common.model.Views;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.*;

/**
 * Created by libin on 14-12-9.
 */
public class BriefContentPage <T> implements org.springframework.data.domain.Page<T>,BriefResult {

    private Page<T> originalPage;

    public BriefContentPage(Page originalPage) {
        this.originalPage = originalPage;

    }

    public BriefContentPage(Collection<T> content) {
        List<T> newList=new ArrayList<T>();
        Iterator iterator=content.iterator();
        while (iterator.hasNext()){
            newList.add((T)iterator.next());
        }
        this.originalPage = new PageImpl<T>(newList);
    }

    @Override
    public int getTotalPages() {
        return originalPage.getTotalPages();
    }

    @Override
    public long getTotalElements() {
        return originalPage.getTotalElements();
    }

    @Override
    public int getNumber() {
        return originalPage.getNumber();
    }

    @Override
    public int getSize() {
        return originalPage.getSize();
    }

    @Override
    public int getNumberOfElements() {
        return originalPage.getNumberOfElements();
    }

    @Override
    @JsonView(Views.Brief.class)
    public List<T> getContent() {

        return Collections.unmodifiableList(originalPage.getContent());
    }

    @Override
    public boolean hasContent() {
        return originalPage.hasContent();
    }

    @Override
    @JsonView(Views.Detail.class)
    public Sort getSort() {
        return originalPage.getSort();
    }

    @Override
    public boolean isFirst() {
        return originalPage.isFirst();
    }

    @Override
    public boolean isLast() {
        return originalPage.isLast();
    }

    @Override
    public boolean hasNext() {
        return originalPage.hasNext();
    }

    @Override
    public boolean hasPrevious() {
        return originalPage.hasPrevious();
    }

    @Override
    public Pageable nextPageable() {
        return originalPage.nextPageable();
    }

    @Override
    public Pageable previousPageable() {
        return originalPage.previousPageable();
    }

    @Override
    public Iterator<T> iterator() {
        return originalPage.iterator();
    }
}
