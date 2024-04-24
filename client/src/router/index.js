import {createRouter, createWebHistory} from 'vue-router'
import UserInfo from "@/views/user/UserInfo.vue";

const Login = () => import('../views/LoginPage.vue')
const Home = () => import('../views/HomePage.vue')
const Register = () => import('../views/RegisterPage.vue')
const Test = () => import('../views/TestPage.vue')
const ConsultantsList = () => import('@/views/user/ConsultantsList.vue')
const UserBookingPage = () => import('@/views/user/UserBookingPage.vue')
import BookingManage from "@/views/conslutants/BookingManage.vue"
import ConsultantsLayout from "@/views/ConsultantsLayout.vue";
import UserLayout from "@/views/UserLayout.vue";
import AdminLayout from "@/views/AdminLayout.vue";
import UpdatePsw from "@/views/user/UpdatePassword.vue";
import OnlineConsultation from "@/views/Exchange/OnlineConsultation.vue";
import BookHouse from "@/views/BookManagement/BookHouse.vue";
import OrdersPage from "@/views/BookManagement/OrdersPage.vue";
import CommentPage from "@/views/Exchange/CommentPage.vue";
import CommunityPage from "@/views/Exchange/CommunityPage.vue";
import ConsultantsInfo from "@/views/conslutants/ConsultantsInfo.vue";
import GroupChats from "@/views/Exchange/GroupChats.vue";
import TestA from "@/views/user/Test.vue";
import UserListPage from "@/views/admin/UserListPage.vue";
import BookListPage from "@/views/admin/BookListPage.vue";
import OrderListPage from "@/views/admin/OrderListPage.vue";
import QuestionnairesPage from "@/views/user/QuestionnairesPage.vue";
import ChartsPage from "@/views/Statistics/ChartsPage.vue";
import AppointmentTablePage from "@/views/conslutants/AppointmentTablePage.vue";
import BookingTablePage from "@/views/user/BookingTablePage.vue";
import AuditPage from "@/views/admin/AuditPage.vue";
import ConsultationTablePage from "@/views/conslutants/ConsultationTablePage.vue";
import BookDetailsPage from "@/views/Evaluation/BookEvaluationPage.vue";
import ConsultantsEvaluationPage from "@/views/Evaluation/ConsultantsEvaluationPage.vue";
import AnnouncementPage from "@/views/admin/AnnouncementPage.vue";
import UpgradePage from "@/views/user/UpgradePage.vue";
import NoticeDetailPage from "@/views/Exchange/NoticeDetailPage.vue";
import {getRole} from "@/utils/getCurrentUserInfo.js";

const routes = [
    {
        path: '/login',
        name: 'LoginPage',
        component: Login
    },
    {
        path: '/register',
        name: 'RegisterPage',
        component: Register
    },

    {
        path: '/user', component: UserLayout, children: [
            {
                path: 'home',
                component: Home,
            },
            {
                path: 'info',
                component: UserInfo
            },
            {path: 'consultantsList', component: ConsultantsList},
            {path: 'booking', component: UserBookingPage},
            {path: 'updatePsw', component: UpdatePsw},
            {path: 'consultation', component: OnlineConsultation},
            {path: 'bookHouse', component: BookHouse},
            {path: 'orders', component: OrdersPage},
            {path: 'CommentPage', component: CommentPage},
            {path: 'communityExchange', component: CommunityPage},
            {path: 'groupChats', component: GroupChats},
            {path: 'TestA', component: TestA},
            {path: 'questionnaires', name: '', component: QuestionnairesPage},
            {path: 'bookingTable', component: BookingTablePage},
            {path: 'bookDetails', component: BookDetailsPage},
            {path: 'consultantsDetails', component: ConsultantsEvaluationPage},
            {path: 'upgrade', component: UpgradePage},
            {path: 'noticeDetail', component: NoticeDetailPage}

        ]
    },
    {
        path: '/consultants', component: ConsultantsLayout, children: [
            {path: 'home', component: Home},
            {path: 'bookingManage', component: BookingManage},
            {path: 'consultation', component: OnlineConsultation},
            {path: 'info', component: ConsultantsInfo},
            {path: 'bookHouse', component: BookHouse},
            {path: 'groupChats', component: GroupChats},
            {path: 'orders', component: OrdersPage},
            {path: 'appointmentTable', component: AppointmentTablePage},
            {path: 'TestA', component: TestA},
            {path: 'consultationTable', component: ConsultationTablePage},
            {path: 'charts', component: ChartsPage},
            {path: 'communityExchange', component: CommunityPage},
            {path: 'noticeDetail', component: NoticeDetailPage}
        ]
    },
    {
        path: '/admin', component: AdminLayout, children: [
            {path: 'home', component: Home},
            {path: 'userList', component: UserListPage},
            {path: 'consultantsList', component: ConsultantsList},
            {path: 'bookList', component: BookListPage},
            {path: 'orderList', component: OrderListPage},
            {path: 'charts', component: ChartsPage},
            {path: 'audit', component: AuditPage},
            {path: 'communityExchange', component: CommunityPage},
            {path: 'announcement', component: AnnouncementPage}
        ]
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes
});
router.beforeEach((to, from, next) => {
    const isAuthenticated = !!getRole();
    if (isAuthenticated) {
        const userRole = getRole();

        if (to.path === '/LoginPage') {
            // 如果用户已登录但访问登录页面，则根据角色重定向到相应主页
            if (userRole === 'USER') {
                next('/user/home');
            } else if (userRole === 'CONSULTANT') {
                next('/consultants/home');
            } else if (userRole === 'ADMIN') {
                next('/admin/userList');
            } else {
                next('/');
            }
        } else {
            // 用户已登录且访问其他页面，检查是否具有对应角色权限
            if ((userRole === 'USER' && to.path.startsWith('/user')) ||
                (userRole === 'CONSULTANT' && to.path.startsWith('/consultants')) ||
                (userRole === 'ADMIN' && to.path.startsWith('/admin'))) {
                next(); // 用户角色匹配时放行
            } else {
                // 没有权限，重定向到对应的主页
                if (userRole === 'USER') {
                    next('/user/home');
                } else if (userRole === 'CONSULTANT') {
                    next('/consultants/home');
                } else if (userRole === 'ADMIN') {
                    next('/admin/home');
                } else {
                    next('/');
                }
            }
        }
    } else {
        // 未登录用户访问其他页面时重定向到登录页面
        if (to.path === '/login') {
            next();
        } else if (to.path === '/register'){
            next();
        }
        else {
            next('/login');
        }
    }
});
export default router;