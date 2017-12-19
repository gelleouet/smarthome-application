<nav class="aui-navgroup aui-navgroup-horizontal">
    <div class="aui-navgroup-inner">
        <div class="aui-navgroup-primary">
            <ul class="aui-nav">
                <li class="${ actionName == 'userFriendFollowing' ? 'aui-nav-selected' : '' }"><g:link action="userFriendFollowing">Amis</g:link></li>
                <li class="${ actionName == 'userFriendSearch' ? 'aui-nav-selected' : '' }"><g:link action="userFriendSearch">Communauté</g:link></li>
            </ul>
        </div><!-- .aui-navgroup-primary -->
    </div><!-- .aui-navgroup-inner -->
</nav>
