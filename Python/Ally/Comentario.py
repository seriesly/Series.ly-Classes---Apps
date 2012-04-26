# -*- coding: utf-8 -*-


    ## Clase que representa un Comentario.
    #
    #  @author Ángel Luis Perales Gómez @ RdlP <angelluispg89@hotmail.com><angelluis.perales@um.es>
    #  @version 0.80
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


class Comentario:
    
    ## Constructor de la Clase.
    #
    #  @param comment Texto del comentario .
    #  @param created Fecha de cuando fue creado el comentario. 
    #  @param author Autor del comentario.
    #  @param idd ID del recurso (película, usuario, serie).
    #  @param totalVotes lista con los votos a favor y en contra. 
    #  @param subcomments Subcomentarios si es que los hay. 
    
    def __init__(self, comment, created, author, idd, totalVotes, subcomments=None):
        self.__comment = comment
        self.__created = created
        self.__author = author
        self.__idd = idd
        self.__totalVotes = totalVotes
        self.__subcomments = subcomments
        #if (subcomments != "None"):
        #    for i in subcomments:
        #        if len(i) == 5:
        #            self.__subcomments = Comentario(i['comment'], i['created'], i['author'], i['totalVotes'], subcomments="None")
        #        else:
        #            self.__subcomments = Comentario(i['comment'], i['created'], i['author'], i['totalVotes'], i['subcomments'])
       
       
    ## Método que devuelve el texto del comentario.
    #
    #  @return Devuelve un string con el texto del comentario.
     
    def getComment(self):
        return self.__comment
    
    ## Método que devuelve cuando fue creado el comentario.
    #
    #  @return Devuelve un string con la fecha de creación del comentario.
    
    def getCreated(self):
        return self.__created
    
    ## Método que devuelve el autor del comentario.
    #
    #  @return Devuelve un string con el autor del comentario.
    
    def getAuthor(self):
        return self.__author
    
    ## Método que devuelve el ID del recurso (serie, película, usuario) del comentario.
    #
    #  @return Devuelve un string con el ID del recurso (serie, película, usuario) del comentario.
    
    def getId(self):
        return self.__idd
    
    ## Método que devuelve una lista con los votos a favor y en contra del comentario.
    #
    #  @return Devuelve una lista con los votos a favor y en contra del comentario.
    
    def getTotalVotes(self):
        return self.__totalVotes
    
    ## Método que devuelve una lista con los subcomentarios del comentario.
    #
    #  @return Devuelve una lista con los subcomentarios del comentario.
    
    def getSubcomments(self):
        return self.__subcomments