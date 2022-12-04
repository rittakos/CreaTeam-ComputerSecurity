import {ProductDetails} from "../../interfaces/product-details.interface";

export const productDetails: ProductDetails[] = [
  {
    id: 0,
    name: 'test0',
    uploadDate: '2022-12-01',
    price: 15,
    size: {
      width: 20,
      height: 30
    },
    creator: {
      id: 0,
      username: 'user0',
      name: 'User User 0'
    },
    comments: [
      {
        id: 0,
        comment: 'Nice upload!',
        user: {
          id: 1,
          username: 'user1',
          name: 'Dear User 1'
        },
        date: '2022-12-02'
      }
    ],
    purchased: true,
    duration: 6,
    tags: [
      'nice',
      'hills',
      'picture'
    ]
  },
  {
    id: 1,
    name: 'test1',
    uploadDate: '2022-12-02',
    price: 1,
    size: {
      width: 40,
      height: 90
    },
    creator: {
      id: 1,
      username: 'user1',
      name: 'Dear User 1'
    },
    comments: [
      {
        id: 0,
        comment: 'Awesome!',
        user: {
          id: 0,
          username: 'user0',
          name: 'User User 0'
        },
        date: '2022-12-02'
      },
      {
        id: 1,
        comment: 'Smooth!',
        user: {
          id: 2,
          username: 'user2',
          name: 'Habakukk 2'
        },
        date: '2022-12-03'
      }
    ],
    purchased: true,
    duration: 9,
    tags: [
    ]
  },
  {
    id: 2,
    name: 'test2',
    uploadDate: '2022-12-03',
    price: 526,
    size: {
      width: 20,
      height: 30
    },
    creator: {
      id: 1,
      username: 'user1',
      name: 'Dear User 1'
    },
    comments: [
      {
        id: 0,
        comment: 'Nice upload!',
        user: {
          id: 1,
          username: 'user1',
          name: 'Dear User 1'
        },
        date: '2022-12-02'
      }
    ],
    purchased: true,
    duration: 6,
    tags: [
      'nice',
      'hills',
      'picture'
    ]
  },
]
