package br.com.alura.challenge.literalura.service;

public interface IDataConversor {
    <T> T convertData(String json, Class<T> tClass);
}
