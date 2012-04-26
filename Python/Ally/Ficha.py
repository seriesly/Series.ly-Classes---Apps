# -*- coding: utf-8 -*-


    ## Clase que representa una Ficha.
    #
    #  @author Ángel Luis Perales Gómez @ RdlP <angelluispg89@hotmail.com><angelluis.perales@um.es>
    #  @version 0.80
    #
    #  @section NOTAS
    #  Es responsabilidad del usuario usar esta clase como es debido, ya
    #  que hay atributos que no siempre se usan y/o inicializan, como por
    #  ejemplo el atributo year que se usa solo para guardar el año de las
    #  películas y no de las series, de tal forma que cuando una instancia
    #  de Ficha esté almacenando una seríe el uso del método getYear() no
    #  funcionara de forma correcta.
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


class Ficha:
    
    ## Constructor de la clase.
    #
    #  @param title String que representa el titulo de la pelicula/serie.
    #  @param idd String que representa el ID de la serie/película.
    #  @param synopsis String que representa la synopsis de la serie/película.
    #  @param seriesly_score String que representa la puntuación de series.ly.
    #  @param participants_score String que representa la puntuación de los participantes.
    #  @param poster String que representa la URL de la imagen de la serie/película.
    #  @param thumb String que representa la URL del thumb de la serie/película.
    #  @param small_thumb String que representa la URL del small_thumb de la serie/película.
    #  @param year String que representa el año de la serie (<b>Solo disponible para las peliculas</b>).
    #  @param links representa los links de la película (<b>Sólo disponible para las películas</b>).
    #  @param ally Referencia al objeto creado por la clase principal Ally.
    #  @param idSerie String que representa al ID de la serie, usado para obtener los capítulos 
    
    def __init__(self, title, idd, synopsis, seriesly_score, participants_score, poster, thumb, small_thumb, year=None, links=None, ally=None, idSerie=None):
        self.__title = title
        self.__idd = idd
        self.__synopsis = synopsis
        self.__seriesly_score = seriesly_score
        self.__participants_score = participants_score
        self.__poster = poster
        self.__thumb = thumb
        self.__small_thumb = small_thumb
        self.__year = year
        self.__links = links
        self.__ally = ally
        self.__idSerie = idSerie
      
    ## Método que devuelve el título de la serie/película.
    #
    #  @return Devuelve un string con el título de la serie/película.
      
    def getTitle(self):
        return self.__title
    
    ## Método que devuelve el ID de la serie/película.
    #
    #  @return Devuelve un string con el ID de la serie/película.
    
    def getId(self):
        return self.__idd
    
    ## Método que devuelve la synopsis de la serie/película.
    #
    #  @return Devuelve un string con la synopsis de la serie/película.
    
    def getSynopsis(self):
        return self.__synopsis
    
    ## Método que devuelve la puntuación de series.ly de la serie/película.
    #
    #  @return Devuelve un string con la puntuación de series.ly de la serie/película.
    
    def getSerieslyScore(self):
        return self.__seriesly_score
    
    ## Método que devuelve la puntuación de los participantes de la serie/película.
    #
    #  @return Devuelve un string con la puntuación de los participantes de la serie/película.
    
    def getParticipantsScore(self):
        return self.__participants_score
    
    ## Método que devuelve la URL de la imagen de la serie/película.
    #
    #  @return Devuelve un string con la URL de la imagen de la serie/película.
    
    def getPoster(self):
        return self.__poster
    
    ## Método que devuelve la URL del thumb de la serie/película.
    #
    #  @return Devuelve un string con la URL del thumb de la serie/película.
    
    def getThumb(self):
        return self.__thumb
    
    ## Método que devuelve la URL del small_thumb de la serie/película.
    #
    #  @return Devuelve un string con la URL del small_thumb de la serie/película.
    
    def getSmallThumb(self):
        return self.__small_thumb
    
    ## Método que devuelve el año de la película.
    #
    #  @return Devuelve un string con el año de la película.
    
    def getYear(self):
        return self.__year
    
    ## Método que devuelve los links de la película.
    #
    #  @return Devuelve un string con los links de la película.
    
    def getLinks(self):
        return self.__links
    
    ## Método que devuelve los capítulos de la serie.
    #
    #  @return Devuelve un string con los capítulos de la serie.
    
    def getCapitulos(self):
        return self.__ally.caps_serie(self.__idSerie)