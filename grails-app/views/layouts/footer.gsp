<footer class="footer">
	<div class="container-fluid">
		<div class="row">
			<div class="col text-left">
		     	
		    </div>
			<div class="col">
		    	<h6><g:meta name="app.code"/> v<g:meta name="app.version"/></h6>
				<asset:image src="/gandi-ssl.png" />
				
				<ul class="list-inline" style="margin-top:10px;">
					<g:if test="${ grailsApplication.config.social.web }">
						<li class="list-inline-item">
							<a class="btn mb-1 btn-primary" href="${ grailsApplication.config.social.web }" target="social"><asset:image src="granddefi/bonhomme.png" height="16px"/> Grand Défi</a>
						</li>
					</g:if>
					<g:if test="${ grailsApplication.config.social.twitter }">
						<li class="list-inline-item">
							<a class="btn mb-1 btn-twitter" href="${ grailsApplication.config.social.twitter }" target="social"><i class="align-middle fab fa-twitter"></i> Twitter</a>
						</li>
					</g:if>
					<g:if test="${ grailsApplication.config.social.facebook }">
						<li class="list-inline-item">
							<a class="btn mb-1 btn-facebook" href="${ grailsApplication.config.social.facebook }" target="social"><i class="align-middle fab fa-facebook"></i> Facebook</a>
						</li>
					</g:if>
					<g:if test="${ grailsApplication.config.social.instagram }">
						<li class="list-inline-item">
							<a class="btn mb-1 btn-instagram" href="${ grailsApplication.config.social.instagram }" target="social"><i class="align-middle fab fa-instagram"></i> Instagram</a>
						</li>
					</g:if>
				</ul>
		    </div>
			<div class="col text-right">
		    	
		    </div>
		</div>
	</div>
	
</footer>