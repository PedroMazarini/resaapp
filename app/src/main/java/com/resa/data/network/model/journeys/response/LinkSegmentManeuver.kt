package com.resa.data.network.model.journeys.response

/**
 * Specifies a maneuver to be executed for a link segment.
 *
 * Values: none,from,to,on,left,right,keepleft,keepright,halfleft,halfright,keephalfleft,keephalfright,sharpleft,sharpright,keepsharpleft,keepsharpright,straight,uturn,enter,leave,enterroundabout,stayinroundabout,leaveroundabout,enterferry,leaveferry,changehighway,checkinferry,checkoutferry,elevator,elevatordown,elevatorup,escalator,escalatordown,escalatorup,stairs,stairsdown,stairsup
 */
enum class LinkSegmentManeuver {
    none,
    from,
    to,
    on,
    left,
    right,
    keepleft,
    keepright,
    halfleft,
    halfright,
    keephalfleft,
    keephalfright,
    sharpleft,
    sharpright,
    keepsharpleft,
    keepsharpright,
    straight,
    uturn,
    enter,
    leave,
    enterroundabout,
    stayinroundabout,
    leaveroundabout,
    enterferry,
    leaveferry,
    changehighway,
    checkinferry,
    checkoutferry,
    elevator,
    elevatordown,
    elevatorup,
    escalator,
    escalatordown,
    escalatorup,
    stairs,
    stairsdown,
    stairsup,
}
