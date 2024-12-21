/* eslint-disable import/no-extraneous-dependencies */
import {js2xml, xml2json} from 'xml-js';

export type Coordinates = {
    x: string;
    y: string;
};

export type TeamMember = {
    id: string;
    name: string;
    coordinates: Coordinates;
    creationDate: string;
    realHero: boolean;
    hasToothpick: boolean;
    impactSpeed: string;
    minutesOfWaiting: string;
    weaponType: string | null;
    mood: string;
    carCool: boolean;
};

export type ParsedResponse = {
    teamList: TeamMember[];
};

export const parseAllRequestFromXml = (xml: string) => {
    const originalResponse = JSON.parse(xml2json(xml, {compact: true}));
    if (originalResponse.allResponseDto.teamList.humanBeingDto === undefined) {
        return {
            teamList: [],
        };
    }
    if (originalResponse.allResponseDto.teamList.humanBeingDto.length) {
        const teamList = originalResponse.allResponseDto.teamList.humanBeingDto.map(
            (member: any) => ({
                id: member.id._text,
                name: member.name._text,
                coordinates: {
                    x: member.coordinates.x._text,
                    y: member.coordinates.y._text,
                },
                creationDate: member.creationDate._text,
                realHero: member._attributes.realHero === 'true',
                hasToothpick: member._attributes.hasToothpick === 'true',
                impactSpeed: member.impactSpeed._text,
                minutesOfWaiting: member.minutesOfWaiting._text,
                weaponType: member.weaponType._text,
                mood: member.mood._text,
                carCool: member.car._attributes.cool === 'true',
            }),
        );
        return {teamList};
    } else {
        const member = originalResponse.allResponseDto.teamList.humanBeingDto;
        return {
            teamList: [
                {
                    id: member.id._text,
                    name: member.name._text,
                    coordinates: {
                        x: member.coordinates.x._text,
                        y: member.coordinates.y._text,
                    },
                    creationDate: member.creationDate._text,
                    realHero: member._attributes.realHero === 'true',
                    hasToothpick: member._attributes.hasToothpick === 'true',
                    impactSpeed: member.impactSpeed._text,
                    minutesOfWaiting: member.minutesOfWaiting._text,
                    weaponType: member.weaponType._text,
                    mood: member.mood._text,
                    carCool: member.car._attributes.cool === 'true',
                },
            ],
        };
    }
};

export type TeamMap = {
    [x: string]: string[];
};

export const parseTeamResponse = (xml: string) => {
    const originalResponse = JSON.parse(xml2json(xml, {compact: true}));
    console.log(originalResponse);
    return originalResponse.listHumanBeingDto.pairs.pairTeamHumanDto.reduce(
        (acc: TeamMap, pair: any) => {
            if (acc[pair.humanBeingId._text]) {
                acc[pair.humanBeingId._text].push(pair.teamId._text);
            } else {
                Object.assign(acc, {
                    [pair.humanBeingId._text]: [pair.teamId._text],
                });
            }
            return acc;
        },
        {},
    );
};

export const convertToJsonXml = (member: Partial<TeamMember>, parentTag = 'humanBeing') => {
    const humanBeingsXml = {
        type: 'element',
        name: parentTag,
        attributes: {
            realHero: member.realHero?.toString(),
            hasToothpick: member.hasToothpick?.toString(),
        },
        elements: [
            {type: 'element', name: 'id', elements: [{type: 'text', text: member.id}]},
            {type: 'element', name: 'name', elements: [{type: 'text', text: member.name}]},
            {
                type: 'element',
                name: 'coordinates',
                elements: [
                    {
                        type: 'element',
                        name: 'x',
                        elements: [{type: 'text', text: member.coordinates?.x}],
                    },
                    {
                        type: 'element',
                        name: 'y',
                        elements: [{type: 'text', text: member.coordinates?.y}],
                    },
                ],
            },
            {
                type: 'element',
                name: 'creationDate',
                elements: [{type: 'text', text: member.creationDate}],
            },
            {
                type: 'element',
                name: 'impactSpeed',
                elements: [{type: 'text', text: member.impactSpeed}],
            },
            {
                type: 'element',
                name: 'minutesOfWaiting',
                elements: [{type: 'text', text: member.minutesOfWaiting}],
            },
            {
                type: 'element',
                name: 'weaponType',
                elements: [{type: 'text', text: member.weaponType}],
            },
            {type: 'element', name: 'mood', elements: [{type: 'text', text: member.mood}]},
            {
                type: 'element',
                name: 'car',
                attributes: {
                    cool: member.carCool?.toString(),
                },
                elements: [],
            },
        ],
    };

    return js2xml({elements: [humanBeingsXml]}, {compact: false, spaces: 2});
};
