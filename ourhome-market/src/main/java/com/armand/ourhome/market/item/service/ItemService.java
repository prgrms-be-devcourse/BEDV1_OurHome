package com.armand.ourhome.market.item.service;

import com.armand.ourhome.common.error.exception.EntityNotFoundException;
import com.armand.ourhome.domain.item.domain.Company;
import com.armand.ourhome.domain.item.domain.Item;
import com.armand.ourhome.domain.item.repository.CompanyRepository;
import com.armand.ourhome.domain.item.repository.ItemRepository;
import com.armand.ourhome.market.item.dto.ItemDto;
import com.armand.ourhome.market.item.dto.request.RequestSaveItem;
import com.armand.ourhome.market.item.mapper.ItemMapper;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final CompanyRepository companyRepository;
    private final ItemMapper itemMapper = Mappers.getMapper(ItemMapper.class);

    @Transactional
    public ItemDto save(RequestSaveItem request) {
        Item item = toItem(request);

        itemRepository.save(item);

        return itemMapper.toItemDto(item, item.getCompanyName());
    }

    public ItemDto findItemBy(Long itemId) {
        return itemRepository.findById(itemId)
                .map(item -> itemMapper.toItemDto(item, item.getCompany().getName()))
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("상품이 존재하지 않습니다. itemId = {0}", itemId)));
    }

    private Item toItem(RequestSaveItem request) {
        Company company = findCompany(request.getCompanyId());
        return itemMapper.toItem(request, company);
    }

    private Company findCompany(Long companyId) {
        return companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("해당 기업이 존재하지 않습니다. id = {0}", companyId)));
    }
}
