# -*- coding: utf-8 -*-


    ## Clase que representa a un Usuario.
    #
    #  @author Ángel Luis Perales Gómez @ RdlP <angelluispg89@hotmail.com><angelluis.perales@um.es>
    #  @version 0.80
    #
    #  @section NOTAS
    #  Es responsabilidad del usuario usar esta clase como es debido, ya
    #  que hay atributos que no siempre se usan y/o inicializan, como por
    #  ejemplo el atributo nombre que se usa en la función buscar, pero no
    #  en la función user_info, por tanto los objetos Usuario que devuelva
    #  la función user_info no tendrán el campo nombre que valdrá None y
    #  por tanto no se podrá hacer un uso correcto de la función getNombre()
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

class Usuario:
    
    ## Constructor de la clase.
    #
    #  Este objeto se construye a partir del diccionario devuelto por la API
    #
    #  @param nick String que representa el nick del usuario.
    #  @param uid String que representa el uid del usuario.
    #  @param points String que representa los puntos del usuario.
    #  @param invitations String que representa las invitaciones que le quedan al usuario.
    #  @param avatar Url del avatar del usuario.
    #  @param badges Lista que representa los badges del usuario.
    #  @param friends Lista que representa los amigos del usuario.
    #  @param followers Lista que representa los seguidores del usuario.
    #  @param nombre String que representa el nombre del usuario 
    
    def __init__(self, nick, uid, points=None, invitations=None, avatar=None, badges=None, friends=None, followers=None, nombre=None):
        self.__nick = nick
        self.__uid = uid
        self.__points = points
        self.__invitations = invitations
        self.__avatar = avatar
        self.__nombre = nombre
        self.__badges = []
        if (badges != None):
            for i in badges:
                self.__badges.append(i)
        self.__friends = []
        if (friends != None):
            for i in friends:
                self.__friends.append(Usuario(i['nick'], i['uid'], i['avatar']))
        self.__followers = []
        if (followers != None):
            for i in followers:
                self.__followers.append(Usuario(i['nick'], i['uid'], i['avatar']))
    
    ## Método que devuelve el nick del usuario.
    #
    #  @return Devuelve un string con el nick del usuario.
         
    def getNick(self):
        return self.__nick
    
    ## Método que devuelve el uid del usuario.
    #
    #  @return Devuelve un string con el uid del usuario.
    
    def getUid(self):
        return self.__uid
    
    ## Método que devuelve los puntos del usuario.
    #
    #  @return Devuelve los puntos del usuario.
    
    def getPoints(self):
        return self.__points
    
    ## Método que devuelve las invitaciones del usuario.
    #
    #  @return Devuelve las invitaciones del usuario.
    
    def getInvitations(self):
        return self.__invitations
    
    ## Método que devuelve la URL del avatar del usuario.
    #
    #  @return Devuelve un string con la URL del avatar del usuario.
    
    def getAvatar(self):
        return self.__avatar
    
    ## Método que devuelve los badges del usuario.
    #
    #  @return Devuelve una lista con los badges del usuario.
    
    def getBadges(self):
        return self.__badges
    
    ## Método que devuelve los amigos del usuario.
    #
    #  @return Devuelve una lista con los amigos del usuario.
    
    def getFriends(self):
        return self.__friends
    
    ## Método que devuelve los seguidores del usuario.
    #
    #  @return Devuelve una lista con los seguidores del usuario.
    
    def getFollowers(self):
        return self.__followers
    
    ## Método que devuelve el nombre del usuario.
    #
    #  @return Devuelve un string con el nombre del usuario.
    
    def getNombre(self):
        return self.__nombre
        