/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.asset.ccat.history_service.mappers;

/**
 *
 * @author wael.mohamed
 */
public interface IMapper<T, P> {

    T mapTo(P param);

}
