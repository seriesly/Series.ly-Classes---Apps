# -*- coding: utf-8 -*-


    ## Clase que representa a una Serie.
    #
    #  @author Ángel Luis Perales Gómez @ RdlP <angelluispg89@hotmail.com><angelluis.perales@um.es>
    #  @version 0.80
    #
    #  @section NOTAS
    #  Es responsabilidad del usuario usar esta clase como es debido, ya
    #  que hay atributos que no siempre se usan y/o inicializan, como por
    #  ejemplo el atributo votos que se usa en la función tops, pero no
    #  en la función user_misSeries, por tanto los objetos Serie que devuelva
    #  la función user_misSeries no tendrán el campo nombre que valdrá None y
    #  por tanto no se podrá hacer un uso correcto de la función getVotos()
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


class Serie:
    
    ## Constructor de la clase
    #
    #  @param idSerie String que representa al ID de la serie.
    #  @param title String que representa al título de la serie.
    #  @param seasons String que representa al número de temporadas de la serie.
    #  @param episodes String que representa al número de episodios de la serie.
    #  @param postr String que representa la URL de la imagen de la serie.
    #  @param thumb String que representa la URL del thumb de la serie.
    #  @param small_thumb String que representa la URL del small thumb de la serie.
    #  @param status String que representa el estado de la pelicula (Puede ser 'Pending', 'Watched' o 'Favourite'.).
    #  @param ally Referencia al objeto creado por la clase principal Ally.
    #  @param idc String que representa el ID del siguiente capítulo por ver.
    #  @param votos String que representa los votos de la serie.
    #  @param num String que representa el siguiente capítulo por ver.
    
    def __init__(self, idSerie, title, seasons=None, episodes=None, postr=None, thumb=None, small_thumb=None, status=None, ally=None, idc=None, votos=None, num=None):
        self.__idSerie = idSerie
        self.__title = title
        self.__seasons = seasons
        self.__episodes = episodes
        self.__postr = postr
        self.__thumb = thumb
        self.__small_thumb = small_thumb
        self.__status = status
        self.__ally = ally
        self.__idc = idc
        self.__votos = votos
        self.__num = num
        
        
    ## Método que devuelve el ID de la serie.
    #
    # @return Devuelve un string con el ID de la serie.
        
    def getIdSerie(self):
        return self.__idSerie
    
    ## Método que devuelve el título de la serie.
    #
    # @return Devuelve un string con el título de la serie.
    
    def getTitle(self):
        return self.__title
    
    ## Método que devuelve el número de temporadas temporadas de la serie.
    #
    # @return Devuelve un string con el número de temporadas de la serie.
    
    def getSeasons(self):
        return self.__seasons
    
    ## Método que devuelve el número de episodios de la serie.
    #
    # @return Devuelve un string con el número de episodios de la serie.
    
    def getEpisodes(self):
        return self.__episodes
    
    ## Método que devuelve la imagen de la serie.
    #
    #  @return Devuelve un string la URL de la imagen de la serie.
    
    def getPostr(self):
        return self.__postr
    
    ## Método que devuelve el thumb de la serie.
    #
    #  @return Devuelve un string la URL del thumb de la serie.
    
    def getThumb(self):
        return self.__thumb
    
    ## Método que devuelve el small thumb de la serie.
    #
    #  @return Devuelve un string con la URL del small_thumb de la serie.
    
    def getSmallThumb(self):
        return self.__small_thumb
    
    ## Método que devuelve el estado de la película.
    #
    #  @return Devuelve un string con el estado de la película (Puede ser 'Pending', 'Watched' o 'Favourite')
    
    def getStatus(self):
        return self.__status
    
    ## Método que devuelve los capítulos de la serie.
    #
    # @return Devuelve los capítulos de la serie.
    
    def getCapitulos(self):
        return self.__ally.caps_serie(self.__idSerie)
    
    ## Método que devuelve el ID del siguiente capítulo a ver.
    # 
    # @return Devuelve un string con el ID del siguiente capítulo a ver.
    
    def getIdc(self):
        return self.__idc
    
    ## Método que devuelve los votos de la serie.
    #
    # @return Devuelve un string con el número de votos de la serie.
    
    def getVotos(self):
        return self.__votos
    
    ## Método que devuelve el número del siguiente capítulo a ver.
    #
    # @return Devuelve un string con el número del siguiente capítulo a ver.
    
    def getNum(self):
        return self.__num