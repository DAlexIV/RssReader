package com.dalexiv.rssreader.domain.di;

/**
 * Created by dalexiv on 8/14/16.
 */

import com.dalexiv.rssreader.domain.interactors.GetChannelByUrlUseCase;
import com.dalexiv.rssreader.domain.interactors.GetChannelsByUrlsUseCase;
import com.dalexiv.rssreader.domain.parsers.ChannelParseModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ChannelParseModule.class)
public interface UseCaseComponent {
    void inject(GetChannelByUrlUseCase useCase);
    void inject(GetChannelsByUrlsUseCase useCase);
}
