import {animate, state, style, transition, trigger} from "@angular/animations";

export const instanceAnimation = trigger('instance', [
  state('changed', style({background: 'blue'})),
  transition(':enter', [
    style({opacity: 0, transform: 'scale(0.7)'}),
    animate('450ms')
  ]),
  transition(':leave', [
    style({opacity: 1}),
    animate('400ms', style({
      opacity: 0,
      transform: 'scale(0.7)'
    }))
  ])
])
