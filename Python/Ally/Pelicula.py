# -*- coding: utf-8 -*-


    ## Clase que representa una Pelicula.
    #
    #  @author Ángel Luis Perales Gómez @ RdlP <angelluispg89@hotmail.com><angelluis.perales@um.es>
    #  @version 0.80
    #
    #  @section NOTAS
    #  Es responsabilidad del usuario usar esta clase como es debido, ya
    #  que hay atributos que no siempre se usan y/o inicializan, como por
    #  ejemplo el atributo any que se usa en la función tops, pero no
    #  en la función user_misPelis, por tanto los objetos Pelicula que devuelva
    #  la función user_misPelis no tendrán el campo any que valdrá None y
    #  por tanto no se podrá hacer un uso correcto de la función getAny()
    #
    #  @section LICENCIA
    #  This program is free software; you can redistribute it and/or
    #  modify it under the terms of the GNU General Public License as
    #  published by the Free Software Foundation; either version 2 of
    #  the License, or (at your option) any later version.
    #
    #  This program is distributed in the hope that it will be useful, but
    #  WITHOUT ANY WARRANTY; without even the implied warranty of
    #  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
    #  General Public License for more details at
    #  http://www.gnu.org/copyleft/gpl.html 


class Pelicula:
    
    ## Constructor de la clase Pelicula
    #
    #  @param idFilm String que representa el ID de la película.
    #  @param title String que representa el título de la película.
    #  @param year String que representa al año de la película.
    #  @param genre String que representa el género de la película.
    #  @param postr String que representa a la imagen de la película.
    #  @param thumb String que representa la URL del thumb de la película.
    #  @param small_thumb String que representa la URL del small_thumb de la película
    #  @param status String que representa el estado de la pelicula (Puede ser 'Pending', 'Watched' o 'Favourite'.).
    #  @param any Parámetro usado en la búsqueda de películas.        
    
    def __init__(self, idFilm, title, year=None, genre=None, postr=None, thumb=None, small_thumb=None, status=None, any=None):
        self.__idFilm = idFilm
        self.__title = title
        self.__year = year
        self.__genre = genre
        self.__postr = postr
        self.__thumb = thumb
        self.__small_thumb = small_thumb
        self.__status = status
        self.__any = any
        
    ## Método que devuelve el ID de la película.
    #
    # @return Devuelve un string con el ID de la película.
        
    def getIdFilm(self):
        return self.__idFilm
    
    ## Método que devuelve el título de la película.
    #
    # @return Devuelve un string con el título de la película.
    
    def getTitle(self):
        return self.__title
    
    ## Método que devuelve el año de la película.
    #
    # @return Devuelve un string con el año de la película.
    
    def getYear(self):
        return self.__year
    
    ## Método que devuelve el género de la película.
    #
    # @return Devuelve un string con el género de la película.
    
    def getGenre(self):
        return self.__genre
    
    ## Método que devuelve la imagen de la película.
    #
    #  @return Devuelve un string la URL de la imagen de la película.
    
    def getPostr(self):
        return self.__postr
    
    ## Método que devuelve el thumb de la película.
    #
    #  @return Devuelve un string la URL del thumb de la película.
    
    def getThumb(self):
        return self.__thumb
    
    ## Método que devuelve el small thumb de la película.
    #
    #  @return Devuelve un string con la URL del small_thumb de la película.
    
    def getSmallThumb(self):
        return self.__small_thumb
    
    ## Método que devuelve el estado de la película.
    #
    #  @return Devuelve un string con el estado de la película (Puede ser 'Pending', 'Watched' o 'Favourite')
    
    def getStatus(self):
        return self.__status
    
    ## Método que devuelve el any de la película.
    #
    #  @return Devuelve un string con el any de la película.
    
    def getAny(self):
        return self.__any